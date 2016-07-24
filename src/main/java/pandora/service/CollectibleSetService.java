package pandora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.repository.CollectibleSetRepository;

@Service
public class CollectibleSetService {
    
    @Autowired
    private CollectibleSetRepository collectibleSetRepository;
    
}
