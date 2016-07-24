package pandora.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.domain.CollectibleCollection;
import pandora.domain.User;
import pandora.repository.CollectibleCollectionRepository;

@Service
public class CollectibleCollectionService {
    
    @Autowired
    private CollectibleCollectionRepository collectibleCollectionRepository;

    public List<CollectibleCollection> findAll(User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        if(currentUser.isIsAdmin()) {
            return collectibleCollectionRepository.findAll();
        } else {
            return collectibleCollectionRepository.findByUserId(currentUser.getId());
        }
    }

    public CollectibleCollection save(CollectibleCollection collectibleCollection, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        collectibleCollection.setUser(currentUser);
        collectibleCollection = collectibleCollectionRepository.save(collectibleCollection);
        return collectibleCollection;
    }

    public CollectibleCollection findOne(Long id, User currentUser) {
        return collectibleCollectionRepository.findOne(id);
    }

    public CollectibleCollection delete(Long id, User currentUser) {
        CollectibleCollection collectibleCollection = collectibleCollectionRepository.findOne(id);
        collectibleCollectionRepository.delete(id);
        return collectibleCollection;
    }
    
}
