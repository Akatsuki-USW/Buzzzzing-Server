package bokjak.bokjakserver.domain.spot.service;

import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.domain.bookmark.model.SpotBookmark;
import bokjak.bokjakserver.domain.bookmark.repository.SpotBookmarkRepository;
import bokjak.bokjakserver.domain.spot.dto.SpotDto.SpotCardResponse;
import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.spot.model.SpotImage;
import bokjak.bokjakserver.domain.spot.repository.SpotRepository;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SpotService {
    private final UserService userService;
    private final SpotRepository spotRepository;
    private final SpotBookmarkRepository spotBookmarkRepository;

    // 스팟 리스트 조회
    public PageResponse<SpotCardResponse> getSpots(
            Long currentUserId,
            Pageable pageable,
            Long cursorId,
            Long locationId,
            List<Long> categoryIds
    ) {
        User user = userService.getUser(currentUserId);
        Page<Spot> resultPage = spotRepository.getSpots(user.getId(), pageable, cursorId, locationId, categoryIds);

        List<Long> bookmarkedSpotIdList = spotBookmarkRepository.findAllByUser(user).stream()
                .map(it -> it.getSpot().getId()).toList();
        Page<SpotCardResponse> responsePage = resultPage
                .map(it -> SpotCardResponse.of(it, bookmarkedSpotIdList.contains(it.getId())));

        return PageResponse.of(responsePage);
    }

    // 스팟 상세 조회
    // 내가 북마크한 스팟 리스트 조회
    // 내가 쓴 스팟 리스트 조회
    // 스팟 상세 조회
    // 스팟 북마크
    // 스팟 생성   TODO user status check 로직 추가 (AuthService)
    // 스팟 수정
    // 스팟 삭제
}
