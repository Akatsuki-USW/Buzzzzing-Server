package bokjak.bokjakserver.domain.spot.repository;

import bokjak.bokjakserver.domain.spot.model.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SpotRepositoryCustom {
    // 리스트 조회
    Page<Spot> getSpots(
            Long userId,
            Pageable pageable,
            Long cursorId,
            Long locationId,
            List<Long> categoryIds
    );

    // 상세 조회
    Optional<Spot> getSpot(Long spotId);

    // 내가 북마크한 스팟 조회
    Page<Spot> getBookmarked(Pageable pageable, Long cursorId, Long userId);
    // 내가 댓글 단 글 조회
    // 생성
    // 수정
    // 삭제
}
