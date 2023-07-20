package bokjak.bokjakserver.domain.congestion.repository;

import bokjak.bokjakserver.domain.congestion.model.WeeklyCongestionStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyCongestionStatisticRepository extends JpaRepository<WeeklyCongestionStatistic, Long> {
}
