package pandora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.repository.CollectibleItemRepository;

@Service
public class CollectibleItemService {
    
    @Autowired
    private CollectibleItemRepository collectibleItemRepository;
    
}
