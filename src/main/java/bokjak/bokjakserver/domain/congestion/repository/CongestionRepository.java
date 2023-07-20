package bokjak.bokjakserver.domain.congestion.repository;

import bokjak.bokjakserver.domain.congestion.model.Congestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CongestionRepository extends JpaRepository<Congestion, Long> {
    // TODO 정확히 현재는 가져올 수 없다. 가장 최근의 혼잡도 정보를 가져와야 함.

    Optional<Congestion> findTopByLocationIdOrderByObservedAtDesc(Long locationId);

}
