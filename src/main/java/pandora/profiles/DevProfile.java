package pandora.profiles;

import pandora.domain.User;
import pandora.service.UserService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = {"dev", "default"})
public class DevProfile {
    
    @Autowired
    private UserService userService;
    
    @PostConstruct
    public void init(){
        
        // add user
        User u1 = new User();
        u1.setLastName("admin");
        u1.setFirstName("oletus");
        u1.setIsAdmin(true);
        u1.setUsername("a");
        u1.setPassword("a");
        u1.seteMail("a@a.a");
        userService.save(u1);
        
        // add user
        User u2 = new User();
        u2.setLastName("testaaja");
        u2.setFirstName("terttu");
        u2.setIsAdmin(false);
        u2.setUsername("b");
        u2.setPassword("b");
        u2.seteMail("b@b.b");
        userService.save(u2);

    }        
}
