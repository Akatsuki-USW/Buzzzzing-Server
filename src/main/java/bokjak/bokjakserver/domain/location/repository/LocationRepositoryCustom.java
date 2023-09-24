package bokjak.bokjakserver.domain.location.repository;

import bokjak.bokjakserver.domain.congestion.model.CongestionLevel;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.util.queries.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LocationRepositoryCustom {
    Page<Location> search(
            Pageable pageable,
            Long cursorId,
            String keyword,
            List<Long> categoryIds,
            SortOrder congestionLevelSortOrder,
            CongestionLevel cursorCongestionLevel
    );

    Page<Location> getLocations(Pageable pageable,Long cursorId);

    Optional<Location> getLocation(Long locationId);
    Page<Location> getTopOfWeeklyAverageCongestion(Pageable pageable, LocalDateTime start, LocalDateTime end);

    Page<Location> getBookmarked(Pageable pageable, Long cursorId, Long userId);

}
