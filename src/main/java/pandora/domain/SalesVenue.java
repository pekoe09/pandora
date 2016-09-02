package pandora.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class SalesVenue extends BaseModel {
    
    @NotBlank
    @Column(length = 200)
    @Length(max = 200, message = "Nimi voi olla enintään 200 merkkiä pitkä")
    private String name;
    @OneToMany(mappedBy = "salesVenue")
    private List<ItemSighting> itemSightings;
    @ManyToOne
    private User user;

    public SalesVenue() {
        this.itemSightings = new ArrayList<>();
    }
    
    public SalesVenue(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }  

    public List<ItemSighting> getItemSightings() {
        return itemSightings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
