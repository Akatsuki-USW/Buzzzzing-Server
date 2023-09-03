package bokjak.bokjakserver.domain.spot.repository;

import bokjak.bokjakserver.domain.spot.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long>, SpotRepositoryCustom {
}
