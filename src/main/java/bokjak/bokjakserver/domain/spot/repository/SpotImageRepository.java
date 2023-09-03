package bokjak.bokjakserver.domain.spot.repository;

import bokjak.bokjakserver.domain.spot.model.SpotImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotImageRepository extends JpaRepository<SpotImage, Long> {
}
