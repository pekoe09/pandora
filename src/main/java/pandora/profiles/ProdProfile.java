package pandora.profiles;

import pandora.domain.User;
import pandora.service.UserService;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class ProdProfile {
    
    @Autowired
    private UserService userService;
    
    @PostConstruct
    public void init(){
        
        // add default user if no users exist yet
        List<User> users = userService.findAll();
        if(users == null || users.size() == 0){
            User u1 = new User();
            u1.setLastName("admin");
            u1.setFirstName("oletus");
            u1.setIsAdmin(true);
            u1.setUsername("b");
            u1.setPassword("b");
            u1.seteMail("b@b.b");
            userService.save(u1);
        }
    }
}
