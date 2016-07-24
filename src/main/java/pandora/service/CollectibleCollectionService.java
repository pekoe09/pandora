package pandora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.domain.CollectibleCollection;
import pandora.domain.User;
import pandora.repository.CollectibleCollectionRepository;

@Service
public class CollectibleCollectionService {
    
    @Autowired
    private CollectibleCollectionRepository collectibleCollectionRepository;

    public Object findAll(User currentUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public CollectibleCollection save(CollectibleCollection collectibleCollection, User currentUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public CollectibleCollection findOne(Long id, User currentUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public CollectibleCollection delete(Long id, User currentUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
