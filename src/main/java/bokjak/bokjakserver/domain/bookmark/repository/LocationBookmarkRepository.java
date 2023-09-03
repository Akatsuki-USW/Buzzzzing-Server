package bokjak.bokjakserver.domain.bookmark.repository;

import bokjak.bokjakserver.domain.bookmark.model.LocationBookmark;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationBookmarkRepository extends JpaRepository<LocationBookmark, Long> {
    Boolean existsByUserAndLocation(User user, Location location);

    Optional<LocationBookmark> findByUserAndLocation(User user, Location location);

    List<LocationBookmark> findAllByUser(User user);
}
