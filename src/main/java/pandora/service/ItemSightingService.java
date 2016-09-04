package pandora.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.domain.ItemSighting;
import pandora.domain.StoredImage;
import pandora.domain.User;
import pandora.repository.ItemSightingRepository;

@Service
@Transactional
public class ItemSightingService {
    
    @Autowired
    private ItemSightingRepository itemSightingRepository;
    @Autowired
    private SalesVenueService salesVenueService;
    @Autowired
    private CollectibleSlotService collectibleSlotService;
    @Autowired
    private StoredImageService storedImageService;
    @Autowired
    private UserService userService;
    
    public List<ItemSighting> findAll(User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        if(currentUser.isIsAdmin()) {
            return itemSightingRepository.findAll();
        } else {
            return itemSightingRepository.findByUserId(currentUser.getId());
        }
    }
    
    public ItemSighting findOne(Long id, User currentUser) {
        ItemSighting itemSighting = itemSightingRepository.findOne(id);
        if(!currentUser.isIsAdmin() && !itemSighting.getUser().getId().equals(currentUser.getId())) {
            itemSighting = null;
        }
        return itemSighting;
    }
        
    public ItemSighting save(ItemSighting itemSighting, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        itemSighting.setUser(currentUser);
        itemSighting = itemSightingRepository.save(itemSighting);
        salesVenueService.addItemSighting(itemSighting.getSalesVenue().getId(), itemSighting);
        collectibleSlotService.addItemSighting(itemSighting.getCollectibleSlot().getId(), itemSighting);
        userService.addItemSighting(currentUser.getId(), itemSighting);
        return itemSighting;
    }
        
    public ItemSighting delete(Long id, User currentUser) {
        ItemSighting itemSighting = itemSightingRepository.findOne(id);
        if(itemSighting != null) {
            salesVenueService.removeItemSighting(itemSighting);
            collectibleSlotService.removeItemSighting(itemSighting);
            List<Long> removableImageIds = new ArrayList<>();
            for(StoredImage storedImage : itemSighting.getStoredImages()) {
                removableImageIds.add(storedImage.getId());                
            }
            for(Long removableImageId : removableImageIds) {
                storedImageService.delete(removableImageId, currentUser);
            }
            itemSightingRepository.delete(id);
        }
        return itemSighting;
    }

    public ItemSighting addStoredImage(Long id, StoredImage image) {
        ItemSighting itemSighting = itemSightingRepository.findOne(id);
        if(itemSighting == null) {
            throw new IllegalArgumentException("Kohdetta ei löydy!");
        }
        if(!image.getUser().getId().equals(itemSighting.getUser().getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        itemSighting.getStoredImages().add(image);
        return itemSightingRepository.save(itemSighting);
    }

    public void removeStoredImage(StoredImage mainImage) {
        ItemSighting itemSighting = mainImage.getItemSighting();
        if(itemSighting != null) {
            List<StoredImage> matches = 
                    itemSighting.getStoredImages()
                    .stream()
                    .filter(p->p.getId().equals(mainImage.getId()))
                    .collect(Collectors.toList());
            for(StoredImage match : matches) {
                itemSighting.getStoredImages().remove(match);
            }
            itemSightingRepository.save(itemSighting);
        }
    }
}
