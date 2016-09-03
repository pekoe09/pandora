package pandora.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.domain.ItemSighting;
import pandora.domain.StoredImage;
import pandora.domain.User;
import pandora.repository.ItemSightingRepository;

@Service
public class ItemSightingService {
    
    @Autowired
    private ItemSightingRepository itemSightingRepository;
    @Autowired
    private SalesVenueService salesVenueService;
    @Autowired
    private CollectibleSlotService collectibleSlotService;
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
        salesVenueService.removeItemSighting(itemSighting);
        collectibleSlotService.removeItemSighting(itemSighting);
        itemSightingRepository.delete(id);
        return itemSighting;
    }

    void addStoredImage(Long id, StoredImage storedImage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void removeStoredImage(StoredImage mainImage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
