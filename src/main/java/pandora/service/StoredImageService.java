package pandora.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.transaction.Transactional;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pandora.domain.StoredImage;
import pandora.domain.User;
import pandora.repository.StoredImageRepository;

@Service
@Transactional
public class StoredImageService {
    
    @Autowired
    private StoredImageRepository storedImageRepository;
    @Autowired
    private AWSService awsService;
    @Autowired
    private CollectibleSlotService collectibleSlotService;
    @Autowired
    private CollectibleItemService collectibleItemService;

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
        if(storedImage.getCollectibleItem() != null) {
            collectibleItemService.addStoredImage(storedImage.getCollectibleItem().getId(), storedImage);
        } else {
            collectibleSlotService.addStoredImage(storedImage.getCollectibleSlot().getId(), storedImage);
        }
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
    
    public Image getImage(Long id, boolean thumbnailed) throws IOException {
        Image image = awsService.retrieve(id.toString());
        if(thumbnailed) {
            image = Thumbnails.of((BufferedImage)image)
                    .size(200, 200)
                    .outputFormat("jpg")
                    .asBufferedImage();
        }
        return image;
    }
}
