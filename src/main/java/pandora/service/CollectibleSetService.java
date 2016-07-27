package pandora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.domain.CollectibleSet;
import pandora.domain.User;
import pandora.repository.CollectibleSetRepository;

@Service
public class CollectibleSetService {
    
    @Autowired
    private CollectibleSetRepository collectibleSetRepository;

    public CollectibleSet save(CollectibleSet collectibleSet, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        collectibleSet.setUser(currentUser);
        collectibleSet = collectibleSetRepository.save(collectibleSet);
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
    
}
