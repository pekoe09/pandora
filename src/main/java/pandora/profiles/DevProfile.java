package pandora.profiles;

import pandora.domain.User;
import pandora.service.UserService;
import javax.annotation.PostConstruct;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pandora.domain.CollectibleCollection;
import pandora.domain.CollectibleItem;
import pandora.domain.CollectibleSet;
import pandora.domain.CollectibleSlot;
import pandora.service.CollectibleCollectionService;
import pandora.service.CollectibleItemService;
import pandora.service.CollectibleSetService;
import pandora.service.CollectibleSlotService;

@Configuration
@Profile(value = {"dev", "default"})
public class DevProfile {
    
    @Autowired
    private UserService userService;
    @Autowired
    private CollectibleCollectionService collectibleCollectionService;
    @Autowired
    private CollectibleSetService collectibleSetService;
    @Autowired
    private CollectibleSlotService collectibleSlotService;
    @Autowired
    private CollectibleItemService collectibleItemService;
    
    @PostConstruct
    public void init(){
        
        // add user
        User u1 = new User();
        u1.setLastName("admin");
        u1.setFirstName("oletus");
        u1.setIsAdmin(true);
        u1.setUsername("a");
        u1.setPassword("a");
        u1.seteMail("a@a.a");
        u1 = userService.save(u1);
        
        // add user
        User u2 = new User();
        u2.setLastName("testaaja");
        u2.setFirstName("terttu");
        u2.setIsAdmin(false);
        u2.setUsername("b");
        u2.setPassword("b");
        u2.seteMail("b@b.b");
        u2 = userService.save(u2);
        
        // add collection
        CollectibleCollection collection = new CollectibleCollection();
        collection.setName("Joku kokoelma");
        collection.setUser(u1);
        collection = collectibleCollectionService.save(collection, u1);
        
        // add set to collection
        CollectibleSet collectibleSet = new CollectibleSet();
        collectibleSet.setName("Joku setti");
        collectibleSet.setUser(u1);
        collectibleSet.setOrdinality(1);
        collectibleSet.setCollectibleCollection(collection);
        collectibleSet.setDescription("Tämä setti sisältää joukon jotain slotteja.");
        collectibleSet = collectibleSetService.save(collectibleSet, u1);

        // add slot to set
        CollectibleSlot collectibleSlot = new CollectibleSlot();
        collectibleSlot.setName("Joku slotti");
        collectibleSlot.setOrdinality(1);
        collectibleSlot.setDescription("Tämä slotti on tila joillekin kohde-esineille.");
        collectibleSlot.setCollectibleSet(collectibleSet);
        collectibleSlot.setUser(u1);
        collectibleSlot = collectibleSlotService.save(collectibleSlot, u1);
        
        // add item to slot
        CollectibleItem collectibleItem = new CollectibleItem();
        collectibleItem.setComment("Joskus hankittu jostain.");
        collectibleItem.setAcquisitionPrice(15.00f);
        collectibleItem.setAcquisitionDate(new DateTime(2015, 11, 3, 0, 0));
        collectibleItem.setCollectibleSlot(collectibleSlot);
        collectibleItem.setUser(u1);
        collectibleItem = collectibleItemService.save(collectibleItem, u1);
    }        
}
