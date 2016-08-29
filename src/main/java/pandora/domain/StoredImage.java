package pandora.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.validator.constraints.Length;

@Entity
public class StoredImage extends BaseModel {
    
    @Column(length = 200)
    @Length(max = 200, message = "Kuvan nimi voi olla enintään 200 merkkiä pitkä")
    private String name;
    @Column(length = 100)
    @Length(max = 100, message = "Kuvateksti voi olla enintään 100 merkkiä pitkä")
    private String caption;
    private boolean isThumbnail;
    @OneToOne
    private StoredImage mainImage;
    @OneToOne
    private StoredImage thumbnailImage;
    @ManyToOne
    private CollectibleSlot collectibleSlot;
    @ManyToOne
    private CollectibleItem collectibleItem;
    @ManyToOne
    private User user;
    
    public StoredImage(){
        setIsThumbnail(false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }   

    public boolean getIsThumbnail() {
        return isThumbnail;
    }

    public void setIsThumbnail(boolean isThumbnail) {
        this.isThumbnail = isThumbnail;
    }

    public StoredImage getMainImage() {
        return mainImage;
    }

    public void setMainImage(StoredImage mainImage) {
        this.mainImage = mainImage;
    }

    public StoredImage getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(StoredImage thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
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
