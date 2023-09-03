package bokjak.bokjakserver.domain.congestion.repository;

import bokjak.bokjakserver.domain.congestion.model.DailyCongestionStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DailyCongestionStatisticRepository extends JpaRepository<DailyCongestionStatistic, Long> {
    Optional<DailyCongestionStatistic> findTop1ByLocationIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long locationId, LocalDateTime start, LocalDateTime end);
}
