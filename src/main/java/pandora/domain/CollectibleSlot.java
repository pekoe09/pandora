package pandora.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class CollectibleSlot extends BaseModel {
    
    @NotBlank
    @Column(length = 200)
    @Length(max = 200, message = "Nimi voi olla enintään 200 merkkiä pitkä")
    private String name;
    @Column(length = 1000)
    @Length(max = 1000, message = "Kuvaus voi olla enintään 1000 merkkiä pitkä")
    private String description;
    @NotNull
    @Min(value = 1, message = "Järjestysnumeron on oltava vähintään 1")
    private Integer ordinality;
    @OneToMany(mappedBy = "collectibleSlot")
    private List<StoredImage> storedImages;
    private long mainImageId;
    @OneToMany(mappedBy = "collectibleSlot")
    private List<CollectibleItem> collectibleItems;
    @OneToMany(mappedBy = "collectibleSlot")
    private List<ItemSighting> itemSightings;
    @ManyToOne
    private CollectibleSet collectibleSet;
    @ManyToOne
    private User user;
    
    public CollectibleSlot() {
        this.storedImages = new ArrayList<>();
        this.collectibleItems = new ArrayList<>();
        this.itemSightings = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrdinality() {
        return ordinality;
    }

    public void setOrdinality(Integer ordinality) {
        this.ordinality = ordinality;
    }

    public List<StoredImage> getStoredImages() {
        return storedImages;
    }

    public long getMainImageId() {
        return mainImageId;
    }

    public void setMainImageId(long mainImageId) {
        this.mainImageId = mainImageId;
    }

    public List<CollectibleItem> getCollectibleItems() {
        return collectibleItems;
    }

    public List<ItemSighting> getItemSightings() {
        return itemSightings;
    }

    public CollectibleSet getCollectibleSet() {
        return collectibleSet;
    }

    public void setCollectibleSet(CollectibleSet collectibleSet) {
        this.collectibleSet = collectibleSet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
