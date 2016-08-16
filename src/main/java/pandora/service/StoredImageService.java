package pandora.service;

import java.awt.Image;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pandora.domain.StoredImage;
import pandora.domain.User;
import pandora.repository.StoredImageRepository;

@Service
public class StoredImageService {
    
    @Autowired
    private StoredImageRepository storedImageRepository;
    @Autowired
    private AWSService awsService;

    public StoredImage save(StoredImage storedImage, MultipartFile image, User currentUser) throws IOException {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        if(image == null) {
            throw new IllegalArgumentException("Kuva puuttuu!");
        }
        storedImage.setUser(currentUser);
        storedImage.setName(image.getOriginalFilename());
        storedImage = storedImageRepository.save(storedImage);
        awsService.deposit(storedImage, image);       
        return storedImage;
    }
    
    public StoredImage findOne(Long id, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        StoredImage storedImage = storedImageRepository.findOne(id);
        if(storedImage != null) {
            if(!currentUser.isIsAdmin() && !storedImage.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalArgumentException("Käyttäjällä ei ole oikeuksia tähän objektiin!");
            }
        }
        return storedImage;
    }    
    
    public Image getImage(Long id) throws IOException {
        return awsService.retrieve(id.toString());
    }
}
