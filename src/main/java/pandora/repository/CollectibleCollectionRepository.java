package pandora.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pandora.domain.CollectibleCollection;

public interface CollectibleCollectionRepository extends JpaRepository<CollectibleCollection, Long> {

    public List<CollectibleCollection> findByUserId(Long id);
    
}
