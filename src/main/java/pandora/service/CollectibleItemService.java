package pandora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.domain.CollectibleItem;
import pandora.domain.User;
import pandora.repository.CollectibleItemRepository;

@Service
public class CollectibleItemService {
    
    @Autowired
    private CollectibleItemRepository collectibleItemRepository;

    public CollectibleItem save(CollectibleItem collectibleItem, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        collectibleItem.setUser(currentUser);
        collectibleItem = collectibleItemRepository.save(collectibleItem);
        return collectibleItem;
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
