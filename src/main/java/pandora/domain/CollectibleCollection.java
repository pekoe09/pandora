package pandora.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Entity
public class CollectibleCollection extends BaseModel {
    
    @NotBlank
    @Column(length = 100)
    @Length(max = 100, message = "Nimi voi olla enintään 100 merkkiä pitkä")
    private String name;
    @Range(min=0, max=1000)
    private int thumbnailHeight;
    @OneToMany(mappedBy = "collectibleCollection")
    private List<CollectibleSet> collectibleSets;
    @ManyToOne
    private User user;
    
    public CollectibleCollection() {
        this.collectibleSets = new ArrayList<>();
        this.thumbnailHeight = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    public void setThumbnailHeight(int thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

    public List<CollectibleSet> getCollectibleSets() {
        return collectibleSets;
    }

    public void setCollectibleSets(List<CollectibleSet> collectibleSets) {
        this.collectibleSets = collectibleSets;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }    
    
}
