package pandora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pandora.domain.CollectibleCollection;

public interface CollectibleCollectionRepository extends JpaRepository<CollectibleCollection, Long> {
    
}
