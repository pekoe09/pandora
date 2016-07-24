package pandora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.repository.CollectibleSlotRepository;

@Service
public class CollectibleSlotService {
    
    @Autowired
    private CollectibleSlotRepository collectibleSlotRepository;
    
}
