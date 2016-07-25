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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public CollectibleSet findOne(Long parentSetId, User currentUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
