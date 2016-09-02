package pandora.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pandora.domain.SalesVenue;

public interface SalesVenueRepository extends JpaRepository<SalesVenue, Long>{

    public List<SalesVenue> findByUserId(Long id);
    
}
