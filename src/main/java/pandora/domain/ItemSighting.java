package pandora.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class ItemSighting extends BaseModel {
    
    @ManyToOne
    private CollectibleSlot collectibleSlot;
    @ManyToOne
    private SalesVenue salesVenue;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private DateTime sightingDate;
    @Min(0)
    private Float price;
    @OneToMany(mappedBy = "itemSighting")
    private List<StoredImage> storedImages;
    @ManyToOne
    private User user;

    public ItemSighting() {
    }
    
    public ItemSighting(CollectibleSlot collectibleSlot, SalesVenue salesVenue, DateTime sightingDate, Float price) {
        this.collectibleSlot = collectibleSlot;
        this.salesVenue = salesVenue;
        this.sightingDate = sightingDate;
        this.price = price;
        this.storedImages = new ArrayList<>();
    }

    public CollectibleSlot getCollectibleSlot() {
        return collectibleSlot;
    }

    public void setCollectibleSlot(CollectibleSlot collectibleSlot) {
        this.collectibleSlot = collectibleSlot;
    }

    public SalesVenue getSalesVenue() {
        return salesVenue;
    }

    public void setSalesVenue(SalesVenue salesVenue) {
        this.salesVenue = salesVenue;
    }

    public DateTime getSightingDate() {
        return sightingDate;
    }

    public void setSightingDate(DateTime sightingDate) {
        this.sightingDate = sightingDate;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public List<StoredImage> getStoredImages() {
        return storedImages;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
