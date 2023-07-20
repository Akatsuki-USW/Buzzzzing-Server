package bokjak.bokjakserver.domain.location.repository;

import bokjak.bokjakserver.domain.congestion.model.CongestionLevel;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.util.queries.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface LocationRepositoryCustom {
    Page<Location> search(
            Pageable pageable,
            Long cursorId,
            String keyword,
            List<Long> categoryIds,
            SortOrder congestionLevelSortOrder,
            CongestionLevel cursorCongestionLevel
    );

    Page<Location> getTopOfWeeklyAverageCongestion(Pageable pageable, LocalDateTime start, LocalDateTime end);

    Page<Location> getBookmarked(Pageable pageable, Long userId);

}
