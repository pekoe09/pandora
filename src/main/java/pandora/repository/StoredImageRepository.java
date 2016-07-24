package pandora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pandora.domain.StoredImage;

public interface StoredImageRepository extends JpaRepository<StoredImage, Long> {
    
}
