package pandora.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.domain.CollectibleSet;
import pandora.domain.CollectibleSlot;
import pandora.domain.User;
import pandora.repository.CollectibleSetRepository;

@Service
@Transactional
public class CollectibleSetService {
    
    @Autowired
    private CollectibleSetRepository collectibleSetRepository;
    @Autowired
    private CollectibleCollectionService collectibleCollectionService;
    @Autowired
    private CollectibleSlotService collectibleSlotService;
    @Autowired
    private UserService userService;

    public CollectibleSet save(CollectibleSet collectibleSet, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        collectibleSet.setUser(currentUser);
        collectibleSet = collectibleSetRepository.save(collectibleSet);
        collectibleCollectionService.addCollectibleSet(collectibleSet.getCollectibleCollection().getId(), collectibleSet);
        userService.addCollectibleSet(currentUser.getId(), collectibleSet);
        if(collectibleSet.getParentSet() != null) {
            addCollectibleSet(collectibleSet.getParentSet().getId(), collectibleSet);
        }
        return collectibleSet;
    }

    public CollectibleSet findOne(Long id, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        CollectibleSet collectibleSet = collectibleSetRepository.findOne(id);
        if(collectibleSet == null) {
            return null;
        }
        if(currentUser.isIsAdmin() || collectibleSet.getUser().getId().equals(currentUser.getId())) {
            return collectibleSetRepository.findOne(id);
        } else {
            throw new IllegalArgumentException("Käyttäjllä ei ole oikeuksia tähän objektiin!");
        }        
    }
    
    public CollectibleSet addCollectibleSet(Long id, CollectibleSet subSet) {
        CollectibleSet collectibleSet = collectibleSetRepository.findOne(id);
        if(collectibleSet == null) {
            throw new IllegalArgumentException("Settiä ei löydy!");
        }
        if(!subSet.getUser().getId().equals(collectibleSet.getUser().getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        List<CollectibleSet> sets = collectibleSet.getChildSets();
        sets.add(subSet);
        collectibleSet.setChildSets(sets);
        collectibleSet = collectibleSetRepository.save(collectibleSet);
        return collectibleSet;
    }
    
    public CollectibleSet addCollectibleSlot(Long id, CollectibleSlot slot) {
        CollectibleSet collectibleSet = collectibleSetRepository.findOne(id);
        if(collectibleSet == null) {
            throw new IllegalArgumentException("Settiä ei löydy!");
        }
        if(!slot.getUser().getId().equals(collectibleSet.getUser().getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        List<CollectibleSlot> slots = collectibleSet.getCollectibleSlots();
        slots.add(slot);
        collectibleSet.setCollectibleSlots(slots);
        collectibleSet = collectibleSetRepository.save(collectibleSet);
        return collectibleSet;
    }
    
    public CollectibleSet delete(Long id, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        CollectibleSet collectibleSet = findOne(id, currentUser);
        if(collectibleSet != null) {
            if(collectibleSet.getParentSet() != null) {
                removeChildSet(collectibleSet);
            }
            for(CollectibleSet childSet : collectibleSet.getChildSets()) {
                childSet.setParentSet(null);
                collectibleSetRepository.save(childSet);
            }
            collectibleCollectionService.removeCollectibleSet(collectibleSet);
            List<Long> removableSlotIds = new ArrayList<>();
            for(CollectibleSlot collectibleSlot : collectibleSet.getCollectibleSlots()) {
                removableSlotIds.add(collectibleSlot.getId());                
            }
            for(Long removableSlotId : removableSlotIds) {
                collectibleSlotService.delete(removableSlotId, currentUser);
            }
            collectibleSetRepository.delete(id);
        }
        return collectibleSet;
    }

    public void removeCollectibleSlot(CollectibleSlot collectibleSlot) {
        CollectibleSet collectibleSet = collectibleSlot.getCollectibleSet();
        if(collectibleSet != null) {
            List<CollectibleSlot> matches = 
                    collectibleSet.getCollectibleSlots()
                    .stream()
                    .filter(p->p.getId().equals(collectibleSlot.getId()))
                    .collect(Collectors.toList());
            for(CollectibleSlot match : matches) {
                collectibleSet.getCollectibleSlots().remove(match);
            }
            collectibleSetRepository.save(collectibleSet);
        }
    }

    private void removeChildSet(CollectibleSet collectibleSet) {
        CollectibleSet parent = collectibleSet.getParentSet();
        if(parent != null) {
            List<CollectibleSet> matches =
                    parent.getChildSets()
                    .stream()
                    .filter(p->p.getId().equals(collectibleSet.getId()))
                    .collect(Collectors.toList());
            for(CollectibleSet match : matches) {
                parent.getChildSets().remove(match);
            }
            collectibleSetRepository.save(parent);
        }
    }
    
}
