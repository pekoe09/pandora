package pandora.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
    private Float price;
    @ManyToOne
    private User user;

    public ItemSighting() {
    }
    
    public ItemSighting(CollectibleSlot collectibleSlot, SalesVenue salesVenue, DateTime sightingDate, Float price) {
        this.collectibleSlot = collectibleSlot;
        this.salesVenue = salesVenue;
        this.sightingDate = sightingDate;
        this.price = price;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
