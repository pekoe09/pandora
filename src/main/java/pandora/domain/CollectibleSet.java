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
public class CollectibleSet extends BaseModel {
    
    @NotBlank
    @Column(length = 100)
    @Length(max = 100, message = "Nimi voi olla enintään 100 merkkiä pitkä")
    private String name;
    @Column(length = 1000)
    @Length(max = 1000, message = "Kuvaus voi olla enintään 1000 merkkiä pitkä")
    private String description;
    @NotNull
    @Min(value = 1, message = "Järjestysnumeron on oltava vähintään 1")
    private Integer ordinality;
    @OneToMany(mappedBy = "collectibleSet")
    private List<CollectibleSlot> collectibleSlots;
    @ManyToOne
    private CollectibleCollection collectibleCollection;
    @OneToMany(mappedBy = "parentSet")
    private List<CollectibleSet> childSets;
    @ManyToOne
    private CollectibleSet parentSet;
    @ManyToOne
    private User user;
    
    public CollectibleSet(){
        this.childSets = new ArrayList<>();
        this.collectibleSlots = new ArrayList<>();
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

    public List<CollectibleSlot> getCollectibleSlots() {
        return collectibleSlots;
    }

    public void setCollectibleSlots(List<CollectibleSlot> collectibleSlots) {
        this.collectibleSlots = collectibleSlots;
    }

    public CollectibleCollection getCollectibleCollection() {
        return collectibleCollection;
    }

    public void setCollectibleCollection(CollectibleCollection collectibleCollection) {
        this.collectibleCollection = collectibleCollection;
    }

    public List<CollectibleSet> getChildSets() {
        return childSets;
    }

    public void setChildSets(List<CollectibleSet> childSets) {
        this.childSets = childSets;
    }

    public CollectibleSet getParentSet() {
        return parentSet;
    }

    public void setParentSet(CollectibleSet parentSet) {
        this.parentSet = parentSet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }    
    
}
