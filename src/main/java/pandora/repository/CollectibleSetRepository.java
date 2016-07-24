package pandora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pandora.domain.CollectibleSet;

public interface CollectibleSetRepository extends JpaRepository<CollectibleSet, Long>{
    
}
