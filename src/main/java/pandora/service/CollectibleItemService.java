package pandora.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.domain.CollectibleItem;
import pandora.domain.StoredImage;
import pandora.domain.User;
import pandora.repository.CollectibleItemRepository;

@Service
@Transactional
public class CollectibleItemService {
    
    @Autowired
    private CollectibleItemRepository collectibleItemRepository;
    @Autowired
    private CollectibleSlotService collectibleSlotService;

    public CollectibleItem save(CollectibleItem collectibleItem, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        collectibleItem.setUser(currentUser);
        collectibleItem = collectibleItemRepository.save(collectibleItem);
        collectibleSlotService.addCollectibleItem(collectibleItem.getCollectibleSlot().getId(), collectibleItem);
        return collectibleItem;
    }
    
    public CollectibleItem addStoredImage(Long id, StoredImage image) {
        CollectibleItem collectibleItem = collectibleItemRepository.findOne(id);
        if(collectibleItem == null) {
            throw new IllegalArgumentException("Kohdetta ei löydy!");
        }
        if(!image.getUser().getId().equals(collectibleItem.getUser().getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        collectibleItem.getStoredImages().add(image);
        if(collectibleItem.getMainImageId() == 0) {
            collectibleItem.setMainImageId(image.getId());
        }
        return collectibleItemRepository.save(collectibleItem);
    }

    public CollectibleItem findOne(Long id, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        CollectibleItem collectibleItem = collectibleItemRepository.findOne(id);
        if(collectibleItem == null) {
            return null;
        }
        if(currentUser.isIsAdmin() || collectibleItem.getUser().getId().equals(currentUser.getId())) {
            return collectibleItem;
        } else {
            throw new IllegalArgumentException("Käyttäjällä ei ole oikeuksia tähän objektiin!");
        }
    }

    public CollectibleItem delete(Long id, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        CollectibleItem collectibleItem = findOne(id, currentUser);
        if(collectibleItem != null) {
            collectibleItemRepository.delete(id);
        }
        return collectibleItem;
    }
    
}
