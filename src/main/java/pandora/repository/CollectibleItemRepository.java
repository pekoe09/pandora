package pandora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pandora.domain.CollectibleItem;

public interface CollectibleItemRepository extends JpaRepository<CollectibleItem, Long> {
    
}
