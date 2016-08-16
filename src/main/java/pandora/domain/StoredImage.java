package pandora.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class StoredImage extends BaseModel {
    
    private String name;
//    private String storageKey;
    @Column(length = 100)
    @Length(max = 100, message = "Kuvateksti voi olla enintään 100 merkkiä pitkä")
    private String caption;
    @ManyToOne
    private CollectibleSlot collectibleSlot;
    @ManyToOne
    private CollectibleItem collectibleItem;
    @ManyToOne
    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//
//    public String getStorageKey() {
//        return storageKey;
//    }
//
//    public void setStorageKey(String storageKey) {
//        this.storageKey = storageKey;
//    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }   

    public CollectibleSlot getCollectibleSlot() {
        return collectibleSlot;
    }

    public void setCollectibleSlot(CollectibleSlot collectibleSlot) {
        this.collectibleSlot = collectibleSlot;
    }    

    public CollectibleItem getCollectibleItem() {
        return collectibleItem;
    }

    public void setCollectibleItem(CollectibleItem collectibleItem) {
        this.collectibleItem = collectibleItem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
