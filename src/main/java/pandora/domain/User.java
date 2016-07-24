package pandora.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Table(name = "pandorauser")
public class User extends BaseModel {
    
    @NotBlank(message = "Sukunimi ei saa olla tyhjä")
    private String lastName;
    @NotBlank(message = "Etunimi ei saa olla tyhjä")
    private String firstName;
    @NotBlank(message = "Sähköpostiosoite ei saa olla tyhjä")
    @Email
    private String eMail;
    private String username;
    @NotBlank(message = "Salasana ei saa olla tyhjä")
    private String password;
    private boolean isAdmin;
    @OneToMany(mappedBy = "user")
    private List<CollectibleCollection> collectibleCollections;
    
    public User(){
        this.collectibleCollections = new ArrayList<>();
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

    public void setCollectibleCollections(List<CollectibleCollection> collectibleCollections) {
        this.collectibleCollections = collectibleCollections;
    }
    
}
