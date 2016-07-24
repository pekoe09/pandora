package pandora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.repository.StoredImageRepository;

@Service
public class StoredImageService {
    
    @Autowired
    private StoredImageRepository storedImageRepository;
    
}
