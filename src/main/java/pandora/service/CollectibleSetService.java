package pandora.service;

import java.util.List;
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

    public CollectibleSet save(CollectibleSet collectibleSet, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        collectibleSet.setUser(currentUser);
        collectibleSet = collectibleSetRepository.save(collectibleSet);
        collectibleCollectionService.addCollectibleSet(collectibleSet.getCollectibleCollection().getId(), collectibleSet);
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
    
}
