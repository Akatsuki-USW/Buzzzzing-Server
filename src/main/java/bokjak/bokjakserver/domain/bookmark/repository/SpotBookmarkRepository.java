package bokjak.bokjakserver.domain.bookmark.repository;

import bokjak.bokjakserver.domain.bookmark.model.SpotBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotBookmarkRepository extends JpaRepository<SpotBookmark, Long> {
    List<SpotBookmark> findAllByUserId(Long userId);
}
