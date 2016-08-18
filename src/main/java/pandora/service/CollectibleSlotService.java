package pandora.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.domain.CollectibleItem;
import pandora.domain.CollectibleSlot;
import pandora.domain.StoredImage;
import pandora.domain.User;
import pandora.repository.CollectibleSlotRepository;

@Service
@Transactional
public class CollectibleSlotService {
    
    @Autowired
    private CollectibleSlotRepository collectibleSlotRepository;
    @Autowired
    private CollectibleSetService collectibleSetService;

    public CollectibleSlot findOne(Long id, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        CollectibleSlot collectibleSlot = collectibleSlotRepository.findOne(id);
        if (collectibleSlot == null) {
            return null;
        }
        if(currentUser.isIsAdmin() || collectibleSlot.getUser().getId().equals(currentUser.getId())) {
            return collectibleSlot;
        } else {
            throw new IllegalArgumentException("Käyttäjällä ei ole oikeuksia tähän objektiin!");
        }
    }

    public CollectibleSlot save(CollectibleSlot collectibleSlot, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        collectibleSlot.setUser(currentUser);
        collectibleSlot = collectibleSlotRepository.save(collectibleSlot);
        collectibleSetService.addCollectibleSlot(collectibleSlot.getCollectibleSet().getId(), collectibleSlot);
        return collectibleSlot;
    }
    
    public CollectibleSlot addCollectibleItem(Long id, CollectibleItem item) {
        CollectibleSlot collectibleSlot = collectibleSlotRepository.findOne(id);
        if(collectibleSlot == null) {
            throw new IllegalArgumentException("Slottia ei löydy!");
        }
        if(!item.getUser().getId().equals(collectibleSlot.getUser().getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        collectibleSlot.getCollectibleItems().add(item);
        return collectibleSlotRepository.save(collectibleSlot);        
    }
    
    public CollectibleSlot addStoredImage(Long id, StoredImage image) {
        CollectibleSlot collectibleSlot = collectibleSlotRepository.findOne(id);
        if(collectibleSlot == null) {
            throw new IllegalArgumentException("Slottia ei löydy!");
        }
        if(!image.getUser().getId().equals(collectibleSlot.getUser().getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        collectibleSlot.getStoredImages().add(image);
        if(collectibleSlot.getMainImageId() == 0) {
            collectibleSlot.setMainImageId(image.getId());
        }
        return collectibleSlotRepository.save(collectibleSlot);
    }

    public CollectibleSlot delete(Long id, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        CollectibleSlot collectibleSlot = findOne(id, currentUser);
        if(collectibleSlot != null) {
            collectibleSlotRepository.delete(id);
        }
        return collectibleSlot;
    }
    
}
