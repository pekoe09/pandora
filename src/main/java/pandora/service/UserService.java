package pandora.service;

import java.util.List;
import pandora.domain.User;
import pandora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pandora.domain.CollectibleCollection;
import pandora.domain.CollectibleItem;
import pandora.domain.CollectibleSet;
import pandora.domain.CollectibleSlot;
import pandora.domain.ItemSighting;
import pandora.domain.SalesVenue;
import pandora.domain.StoredImage;

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

    public User addCollectibleCollection(Long id, CollectibleCollection collectibleCollection) {
        User user = userRepository.findOne(id);
        if(user == null) {
            throw new IllegalArgumentException("Kohdetta ei löydy!");
        }
        if(!collectibleCollection.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        user.getCollectibleCollections().add(collectibleCollection);
        return userRepository.save(user);
    }

    public User addCollectibleSet(Long id, CollectibleSet collectibleSet) {
        User user = userRepository.findOne(id);
        if(user == null) {
            throw new IllegalArgumentException("Kohdetta ei löydy!");
        }
        if(!collectibleSet.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        user.getCollectibleSets().add(collectibleSet);
        return userRepository.save(user);
    }

    public User addCollectibleSlot(Long id, CollectibleSlot collectibleSlot) {
        User user = userRepository.findOne(id);
        if(user == null) {
            throw new IllegalArgumentException("Kohdetta ei löydy!");
        }
        if(!collectibleSlot.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        user.getCollectibleSlots().add(collectibleSlot);
        return userRepository.save(user);
    }

    public User addCollectibleItem(Long id, CollectibleItem collectibleItem) {
        User user = userRepository.findOne(id);
        if(user == null) {
            throw new IllegalArgumentException("Kohdetta ei löydy!");
        }
        if(!collectibleItem.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        user.getCollectibleItems().add(collectibleItem);
        return userRepository.save(user);
    }

    public User addStoredImage(Long id, StoredImage storedImage) {
        User user = userRepository.findOne(id);
        if(user == null) {
            throw new IllegalArgumentException("Kohdetta ei löydy!");
        }
        if(!storedImage.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        user.getStoredImages().add(storedImage);
        return userRepository.save(user);
    }

    public User addItemSighting(Long id, ItemSighting itemSighting) {
        User user = userRepository.findOne(id);
        if(user == null) {
            throw new IllegalArgumentException("Kohdetta ei löydy!");
        }
        if(!itemSighting.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        user.getItemSightings().add(itemSighting);
        return userRepository.save(user);
    }

    public User addSalesVenue(Long id, SalesVenue salesVenue) {
        User user = userRepository.findOne(id);
        if(user == null) {
            throw new IllegalArgumentException("Kohdetta ei löydy!");
        }
        if(!salesVenue.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Käyttäjän oikeudet eivät riitä operaatioon!");
        }
        user.getSalesVenues().add(salesVenue);
        return userRepository.save(user);
    }

    public User delete(Long id) {
        User user = userRepository.findOne(id);
        if(user == null) {
            throw new IllegalArgumentException("Kohdetta ei löydy!");
        }
        try {
            userRepository.delete(id);
        } catch (Exception exc) {
            return null;
        }
        return user;
    }
}
