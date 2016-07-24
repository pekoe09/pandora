package pandora.service;

import java.util.List;
import pandora.domain.User;
import pandora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User getCurrentUser(){
        User user = null;
        try {
            UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
            String username = authToken.getPrincipal().toString();
            user = userRepository.findByUsername(username);
        }
        catch (Exception exc) {
        }

        return user;
    }
    
    public User save(User user){
        return userRepository.save(user);
    }
    
    public List<User> findAll(){
        return userRepository.findAll();
    }
    
    public User findOne(Long id){
        return userRepository.findOne(id);
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
