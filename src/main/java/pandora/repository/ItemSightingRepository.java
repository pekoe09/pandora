package pandora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pandora.domain.ItemSighting;

public interface ItemSightingRepository extends JpaRepository<ItemSighting, Long>{
    
}
