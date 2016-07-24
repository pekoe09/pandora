package pandora.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class CollectibleItem extends BaseModel{
    
    @Column(length = 1000)
    @Length(max = 1000, message = "Kommentti voi olla enintään 1000 merkkiä pitkä")
    private String comment;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private DateTime acquisitionDate;
    private Float acquisitionPrice;
    @ManyToOne
    private CollectibleSlot collectibleSlot;
    @OneToMany(mappedBy = "collectibleItem")
    private List<StoredImage> storedImages;
    
    public CollectibleItem() {
        this.storedImages = new ArrayList<>();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DateTime getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(DateTime acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public Float getAcquisitionPrice() {
        return acquisitionPrice;
    }

    public void setAcquisitionPrice(Float acquisitionPrice) {
        this.acquisitionPrice = acquisitionPrice;
    }

    public CollectibleSlot getCollectibleSlot() {
        return collectibleSlot;
    }

    public void setCollectibleSlot(CollectibleSlot collectibleSlot) {
        this.collectibleSlot = collectibleSlot;
    }

    public List<StoredImage> getStoredImages() {
        return storedImages;
    }

    public void setStoredImages(List<StoredImage> storedImages) {
        this.storedImages = storedImages;
    }
    
}
