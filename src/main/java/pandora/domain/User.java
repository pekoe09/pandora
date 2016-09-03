package pandora.domain;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Table(name = "pandorauser")
public class User extends BaseModel {
    
    @Column(length = 100)
    @Length(max = 100, message = "Sukunimi voi olla enintään 100 merkkiä pitkä")
    @NotBlank(message = "Sukunimi ei saa olla tyhjä")
    private String lastName;
    @Column(length = 100)
    @Length(max = 100, message = "Etunimi voi olla enintään 100 merkkiä pitkä")
    @NotBlank(message = "Etunimi ei saa olla tyhjä")
    private String firstName;
    @Column(length = 300)
    @Length(max = 300, message = "Sähköpostiosoite voi olla enintään 300 merkkiä pitkä")
    @NotBlank(message = "Sähköpostiosoite ei saa olla tyhjä")
    @Email
    private String eMail;
    @Column(length = 100)
    @Length(max = 100, message = "Käyttäjätunnus voi olla enintään 100 merkkiä pitkä")
    @NotBlank(message = "Käyttäjätunnus ei saa olla tyhjä")
    private String username;
    @Column(length = 100)
    @Length(max = 100, message = "Salasana voi olla enintään 100 merkkiä pitkä")
    @NotBlank(message = "Salasana ei saa olla tyhjä")
    private String password;
    private boolean isAdmin;
    @OneToMany(mappedBy = "user")
    private List<CollectibleCollection> collectibleCollections;
    @OneToMany(mappedBy = "user")
    private List<CollectibleSet> collectibleSets;
    @OneToMany(mappedBy = "user")
    private List<CollectibleSlot> collectibleSlots;
    @OneToMany(mappedBy = "user")
    private List<CollectibleItem> collectibleItems;
    @OneToMany(mappedBy = "user")
    private List<ItemSighting> itemSightings;
    @OneToMany(mappedBy = "user")
    private List<SalesVenue> salesVenues;
    @OneToMany(mappedBy = "user")
    private List<StoredImage> storedImages;
    
    public User(){
        this.collectibleCollections = new ArrayList<>();
        this.collectibleSets = new ArrayList<>();
        this.collectibleSlots = new ArrayList<>();
        this.collectibleItems = new ArrayList<>();
        this.itemSightings = new ArrayList<>();
        this.salesVenues = new ArrayList<>();
        this.storedImages = new ArrayList<>();
        this.setIsAdmin(false);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
        this.username = eMail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List<CollectibleCollection> getCollectibleCollections() {
        return collectibleCollections;
    }

    public List<CollectibleSet> getCollectibleSets() {
        return collectibleSets;
    }

    public List<CollectibleSlot> getCollectibleSlots() {
        return collectibleSlots;
    }

    public List<CollectibleItem> getCollectibleItems() {
        return collectibleItems;
    }

    public List<ItemSighting> getItemSightings() {
        return itemSightings;
    }

    public List<SalesVenue> getSalesVenues() {
        return salesVenues;
    }

    public List<StoredImage> getStoredImages() {
        return storedImages;
    }

    
}
