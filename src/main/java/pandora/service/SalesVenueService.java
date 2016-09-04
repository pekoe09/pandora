package pandora.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.domain.ItemSighting;
import pandora.domain.SalesVenue;
import pandora.domain.User;
import pandora.repository.SalesVenueRepository;

@Service
@Transactional
public class SalesVenueService {
    
    @Autowired
    private SalesVenueRepository salesVenueRepository;
    @Autowired
    private ItemSightingService itemSightingService;
    @Autowired
    private UserService userService;
    
    public List<SalesVenue> findAll(User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        if(currentUser.isIsAdmin()) {
            return salesVenueRepository.findAll();
        } else {
            return salesVenueRepository.findByUserId(currentUser.getId());
        }
    }
    
    public SalesVenue findOne(Long id, User currentUser) {
        SalesVenue salesVenue = salesVenueRepository.findOne(id);
        if(!currentUser.isIsAdmin() && !salesVenue.getUser().getId().equals(currentUser.getId())) {
            salesVenue = null;
        }
        return salesVenue;
    }
    
    public SalesVenue save(SalesVenue salesVenue, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        salesVenue.setUser(currentUser);
        salesVenue = salesVenueRepository.save(salesVenue);
        userService.addSalesVenue(currentUser.getId(), salesVenue);
        return salesVenue;
    }
    
    public SalesVenue addItemSighting(Long id, ItemSighting itemSighting) {
        SalesVenue salesVenue = salesVenueRepository.findOne(id);
        if(salesVenue == null) {
            throw new IllegalArgumentException("Myyntipaikkaa ei löydy!");
        }
        if(!itemSighting.getUser().getId().equals(salesVenue.getUser().getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        salesVenue.getItemSightings().add(itemSighting);
        salesVenue = salesVenueRepository.save(salesVenue);
        return salesVenue;
    }
    
    public SalesVenue delete(Long id, User currentUser) {
        SalesVenue salesVenue = salesVenueRepository.findOne(id);
        if(salesVenue != null) {
            List<Long> removableSightingIds = new ArrayList<>();
            for(ItemSighting itemSighting : salesVenue.getItemSightings()) {
                removableSightingIds.add(itemSighting.getId());                
            }
            for(Long removableSightingId : removableSightingIds) {
                itemSightingService.delete(removableSightingId, currentUser);
            }
            salesVenueRepository.delete(id);
        }
        return salesVenue;
    }

    public void removeItemSighting(ItemSighting itemSighting) {
        SalesVenue salesVenue = itemSighting.getSalesVenue();
        if(salesVenue != null) {
            List<ItemSighting> matches = 
                    salesVenue.getItemSightings()
                    .stream()
                    .filter(p->p.getId().equals(itemSighting.getId()))
                    .collect(Collectors.toList());
            for(ItemSighting match : matches) {
                salesVenue.getItemSightings().remove(match);
            }
            salesVenueRepository.save(salesVenue);
        }
    }
}
