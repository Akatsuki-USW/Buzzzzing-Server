package bokjak.bokjakserver.domain.congestion.repository;

import bokjak.bokjakserver.domain.congestion.model.Congestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CongestionRepository extends JpaRepository<Congestion, Long> {
    Optional<Congestion> findTopByLocationIdOrderByObservedAtDesc(Long locationId);
}
