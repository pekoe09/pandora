package pandora.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.domain.CollectibleCollection;
import pandora.domain.CollectibleSet;
import pandora.domain.User;
import pandora.repository.CollectibleCollectionRepository;

@Service
@Transactional
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
    
    public CollectibleCollection addCollectibleSet(Long id, CollectibleSet set) {
        CollectibleCollection collectibleCollection = collectibleCollectionRepository.findOne(id);
        if(collectibleCollection == null) {
            throw new IllegalArgumentException("Kokoelmaa ei löydy!");
        }
        if(!set.getUser().getId().equals(collectibleCollection.getUser().getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        List<CollectibleSet> sets = collectibleCollection.getCollectibleSets();
        sets.add(set);
        collectibleCollection.setCollectibleSets(sets);
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
