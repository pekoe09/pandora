package pandora.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
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
    @Autowired
    private ItemSightingService itemSightingService;
    @Autowired
    private UserService userService;

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
        
        // adds thumbnail version as well
        StoredImage thumbnailImage = new StoredImage();
        thumbnailImage.setCaption(storedImage.getCaption());
        thumbnailImage.setName(storedImage.getName());
        thumbnailImage.setUser(currentUser);
        thumbnailImage.setIsThumbnail(true);
        thumbnailImage.setMainImage(storedImage);
        thumbnailImage.setCollectibleItem(storedImage.getCollectibleItem());
        thumbnailImage.setCollectibleSlot(storedImage.getCollectibleSlot());
        thumbnailImage.setItemSighting(storedImage.getItemSighting());
        thumbnailImage = storedImageRepository.save(thumbnailImage);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int thumbnailHeight = storedImage.getCollectibleItem() != null ? storedImage.getCollectibleItem().getCollectibleSlot().getCollectibleSet().getCollectibleCollection().getThumbnailHeight()
                                                                        : storedImage.getCollectibleSlot().getCollectibleSet().getCollectibleCollection().getThumbnailHeight();
        if(thumbnailHeight == 0) {
            thumbnailHeight = 200;
        }
        Thumbnails.of((BufferedImage)ImageIO.read(new BufferedInputStream(image.getInputStream())))
                                                .height(thumbnailHeight)
                                                .outputFormat("jpg")
                                                .toOutputStream(os);        
        awsService.deposit(thumbnailImage, new ByteArrayInputStream(os.toByteArray()), os.size());
        storedImage.setThumbnailImage(thumbnailImage);
        storedImage = storedImageRepository.save(storedImage);
        
        if(storedImage.getCollectibleItem() != null) {
            collectibleItemService.addStoredImage(storedImage.getCollectibleItem().getId(), storedImage);
            collectibleItemService.addStoredImage(storedImage.getCollectibleItem().getId(), thumbnailImage);
        } else if (storedImage.getCollectibleSlot() != null){
            collectibleSlotService.addStoredImage(storedImage.getCollectibleSlot().getId(), storedImage);
            collectibleSlotService.addStoredImage(storedImage.getCollectibleSlot().getId(), thumbnailImage);
        } else {
            itemSightingService.addStoredImage(storedImage.getItemSighting().getId(), storedImage);
            itemSightingService.addStoredImage(storedImage.getItemSighting().getId(), thumbnailImage);
        }
        userService.addStoredImage(currentUser.getId(), storedImage);
        userService.addStoredImage(currentUser.getId(), thumbnailImage);
        return storedImage;
    }
    
    public StoredImage findOne(Long id, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        if(id == 0) {
            return null;
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
        Image image = null;
        image = awsService.retrieve(id.toString());
        return image;
    }

    public StoredImage delete(Long id, User currentUser) {
        StoredImage image = findOne(id, currentUser);
        if(image == null) {
            return null;
        } 
        if(!currentUser.isIsAdmin() && !image.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Käyttäjällä ei ole oikeuksia tähän objektiin!");
        }
        
        StoredImage mainImage, thumbnailImage;
        if(image.getIsThumbnail())  {
            mainImage = image.getMainImage();
            thumbnailImage = image;
        } else {
            mainImage = image;
            thumbnailImage = image.getThumbnailImage();
        }
        
        try {
            awsService.delete(mainImage);  
            awsService.delete(thumbnailImage);
        } catch (IOException exc) {
            throw new IllegalArgumentException(exc.getMessage());
        }
        if(image.getCollectibleItem() != null) {
            collectibleItemService.removeStoredImage(mainImage);
            collectibleItemService.removeStoredImage(thumbnailImage);
        } else if(image.getCollectibleSlot() != null) {
            collectibleSlotService.removeStoredImage(mainImage);
            collectibleSlotService.removeStoredImage(thumbnailImage);
        } else if(image.getItemSighting() != null) {
            itemSightingService.removeStoredImage(mainImage);
            itemSightingService.removeStoredImage(thumbnailImage);
        }
        mainImage.setThumbnailImage(null);
        thumbnailImage.setMainImage(null);
        storedImageRepository.save(mainImage);
        storedImageRepository.save(thumbnailImage);
        
        storedImageRepository.delete(mainImage.getId());
        storedImageRepository.delete(thumbnailImage.getId());
        return image;
    }

}
