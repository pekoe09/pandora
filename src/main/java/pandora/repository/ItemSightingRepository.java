package pandora.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pandora.domain.ItemSighting;

public interface ItemSightingRepository extends JpaRepository<ItemSighting, Long>{

    public List<ItemSighting> findByUserId(Long id);
    
}
