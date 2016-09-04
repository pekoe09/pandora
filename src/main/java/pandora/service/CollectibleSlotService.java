package pandora.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.domain.CollectibleItem;
import pandora.domain.CollectibleSlot;
import pandora.domain.ItemSighting;
import pandora.domain.SalesVenue;
import pandora.domain.StoredImage;
import pandora.domain.User;
import pandora.repository.CollectibleSlotRepository;

@Service
@Transactional
public class CollectibleSlotService {
    
    @Autowired
    private CollectibleSlotRepository collectibleSlotRepository;
    @Autowired
    private CollectibleSetService collectibleSetService;
    @Autowired
    private StoredImageService storedImageService;
    @Autowired
    private CollectibleItemService collectibleItemService;
    @Autowired
    private ItemSightingService itemSightingService;
    @Autowired
    private UserService userService;

    public CollectibleSlot findOne(Long id, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        CollectibleSlot collectibleSlot = collectibleSlotRepository.findOne(id);
        if (collectibleSlot == null) {
            return null;
        }
        if(currentUser.isIsAdmin() || collectibleSlot.getUser().getId().equals(currentUser.getId())) {
            return collectibleSlot;
        } else {
            throw new IllegalArgumentException("Käyttäjällä ei ole oikeuksia tähän objektiin!");
        }
    }

    public CollectibleSlot save(CollectibleSlot collectibleSlot, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        collectibleSlot.setUser(currentUser);
        collectibleSlot = collectibleSlotRepository.save(collectibleSlot);
        collectibleSetService.addCollectibleSlot(collectibleSlot.getCollectibleSet().getId(), collectibleSlot);
        userService.addCollectibleSlot(currentUser.getId(), collectibleSlot);        
        return collectibleSlot;
    }
    
    public CollectibleSlot addCollectibleItem(Long id, CollectibleItem item) {
        CollectibleSlot collectibleSlot = collectibleSlotRepository.findOne(id);
        if(collectibleSlot == null) {
            throw new IllegalArgumentException("Slottia ei löydy!");
        }
        if(!item.getUser().getId().equals(collectibleSlot.getUser().getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        collectibleSlot.getCollectibleItems().add(item);
        return collectibleSlotRepository.save(collectibleSlot);        
    }
    
    public CollectibleSlot addStoredImage(Long id, StoredImage image) {
        CollectibleSlot collectibleSlot = collectibleSlotRepository.findOne(id);
        if(collectibleSlot == null) {
            throw new IllegalArgumentException("Slottia ei löydy!");
        }
        if(!image.getUser().getId().equals(collectibleSlot.getUser().getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        collectibleSlot.getStoredImages().add(image);
        if(collectibleSlot.getMainImageId() == 0 && image.getIsThumbnail()) {
            collectibleSlot.setMainImageId(image.getId());
        }
        return collectibleSlotRepository.save(collectibleSlot);
    }
    
    public CollectibleSlot addItemSighting(Long id, ItemSighting itemSighting) {
        CollectibleSlot collectibleSlot = collectibleSlotRepository.findOne(id);
        if(collectibleSlot == null) {
            throw new IllegalArgumentException("Slottia ei löydy!");
        }
        if(!itemSighting.getUser().getId().equals(collectibleSlot.getUser().getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        collectibleSlot.getItemSightings().add(itemSighting);
        collectibleSlot = collectibleSlotRepository.save(collectibleSlot);
        return collectibleSlot;
    }

    public CollectibleSlot delete(Long id, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        CollectibleSlot collectibleSlot = findOne(id, currentUser);
        if(collectibleSlot != null) {
            collectibleSetService.removeCollectibleSlot(collectibleSlot);
            List<Long> removableItemIds = new ArrayList<>();
            for(CollectibleItem collectibleItem : collectibleSlot.getCollectibleItems()) {
                removableItemIds.add(collectibleItem.getId());                
            }
            for(Long removableItemId : removableItemIds) {
                collectibleItemService.delete(removableItemId, currentUser);
            }
            List<Long> removableImageIds = new ArrayList<>();
            for(StoredImage storedImage : collectibleSlot.getStoredImages()) {
                removableImageIds.add(storedImage.getId());                
            }
            for(Long removableImageId : removableImageIds) {
                storedImageService.delete(removableImageId, currentUser);
            }
            List<Long> removableSightingIds = new ArrayList<>();
            for(ItemSighting itemSighting : collectibleSlot.getItemSightings()) {
                removableSightingIds.add(itemSighting.getId());                
            }
            for(Long removableSightingId : removableSightingIds) {
                itemSightingService.delete(removableSightingId, currentUser);
            }
            collectibleSlotRepository.delete(id);
        }
        return collectibleSlot;
    }

    public void removeStoredImage(StoredImage image) {
        if(image == null) {
            throw new IllegalArgumentException("Kuva puuttuu!");
        }
        CollectibleSlot collectibleSlot = image.getCollectibleSlot();
        collectibleSlot.getStoredImages().remove(image);
        if (collectibleSlot.getMainImageId() == image.getId()) {
            if(collectibleSlot.getStoredImages().size() > 0) {
                collectibleSlot.setMainImageId(collectibleSlot.getStoredImages().get(0).getId());
            } else {
                collectibleSlot.setMainImageId(0);
            }           
        }
        collectibleSlotRepository.save(collectibleSlot);
    }

    public void removeCollectibleItem(CollectibleItem collectibleItem) {
        CollectibleSlot collectibleSlot = collectibleItem.getCollectibleSlot();
        if(collectibleSlot != null) {
            List<CollectibleItem> matches = 
                    collectibleSlot.getCollectibleItems()
                    .stream()
                    .filter(p->p.getId().equals(collectibleItem.getId()))
                    .collect(Collectors.toList());
            for(CollectibleItem match : matches) {
                collectibleSlot.getCollectibleItems().remove(match);
            }
            collectibleSlotRepository.save(collectibleSlot);
        }
    }

    public void removeItemSighting(ItemSighting itemSighting) {
        CollectibleSlot collectibleSlot = itemSighting.getCollectibleSlot();
        if(collectibleSlot != null) {
            List<ItemSighting> matches = 
                    collectibleSlot.getItemSightings()
                    .stream()
                    .filter(p->p.getId().equals(itemSighting.getId()))
                    .collect(Collectors.toList());
            for(ItemSighting match : matches) {
                collectibleSlot.getItemSightings().remove(match);
            }
            collectibleSlotRepository.save(collectibleSlot);
        }
    }
    
}
