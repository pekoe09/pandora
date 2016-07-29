package pandora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pandora.domain.CollectibleSlot;
import pandora.domain.User;
import pandora.repository.CollectibleSlotRepository;

@Service
public class CollectibleSlotService {
    
    @Autowired
    private CollectibleSlotRepository collectibleSlotRepository;

    public CollectibleSlot findOne(Long id, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        CollectibleSlot collectibleSlot = collectibleSlotRepository.findOne(id);
        if (collectibleSlot == null) {
            return null;
        }
        if(currentUser.isIsAdmin() || collectibleSlot.getUser().getId().equals(currentUser.getId())) {
            return collectibleSlot;
        } else {
            throw new IllegalArgumentException("Käyttäjällä ei ole oikeuksia tähän objektiin!");
        }
    }

    public CollectibleSlot save(CollectibleSlot collectibleSlot, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        collectibleSlot.setUser(currentUser);
        collectibleSlot = collectibleSlotRepository.save(collectibleSlot);
        return collectibleSlot;
    }

    public CollectibleSlot delete(Long id, User currentUser) {
        if(currentUser == null) {
            throw new IllegalArgumentException("Käyttäjätieto puuttuu!");
        }
        CollectibleSlot collectibleSlot = findOne(id, currentUser);
        if(collectibleSlot != null) {
            collectibleSlotRepository.delete(id);
        }
        return collectibleSlot;
    }
    
}
