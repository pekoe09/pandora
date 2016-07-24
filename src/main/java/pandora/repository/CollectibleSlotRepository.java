package pandora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pandora.domain.CollectibleSlot;

public interface CollectibleSlotRepository extends JpaRepository<CollectibleSlot, Long> {
    
}
