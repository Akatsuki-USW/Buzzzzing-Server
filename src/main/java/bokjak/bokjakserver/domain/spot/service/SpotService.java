package bokjak.bokjakserver.domain.spot.service;

import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.bookmark.model.SpotBookmark;
import bokjak.bokjakserver.domain.bookmark.repository.SpotBookmarkRepository;
import bokjak.bokjakserver.domain.spot.dto.SpotDto.BookmarkResponse;
import bokjak.bokjakserver.domain.spot.dto.SpotDto.SpotCardResponse;
import bokjak.bokjakserver.domain.spot.dto.SpotDto.SpotDetailResponse;
import bokjak.bokjakserver.domain.spot.exception.SpotException;
import bokjak.bokjakserver.domain.spot.model.Spot;
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
import java.util.Optional;


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

        return makeSpotCardResponsePageResponse(user, resultPage);
    }

    // 내가 북마크한 스팟 리스트 조회
    public PageResponse<SpotCardResponse> getMyBookmarkedSpots(Long currentUserId, Pageable pageable, Long cursorId) {
        User user = userService.getUser(currentUserId);
        Page<Spot> resultPage = spotRepository.getBookmarked(pageable, cursorId, currentUserId);

        return makeSpotCardResponsePageResponse(user, resultPage);
    }

    private PageResponse<SpotCardResponse> makeSpotCardResponsePageResponse(User user, Page<Spot> resultPage) {
        List<Long> bookmarkedSpotIdList = spotBookmarkRepository.findAllByUser(user).stream()
                .map(it -> it.getSpot().getId()).toList();

        Page<SpotCardResponse> responsePage = resultPage
                .map(it -> SpotCardResponse.of(it, bookmarkedSpotIdList.contains(it.getId())));

        return PageResponse.of(responsePage);
    }

    // 스팟 상세 조회
    public SpotDetailResponse getSpotDetail(Long currentUserId, Long spotId) {
        Spot spot = spotRepository.getSpot(spotId)
                .orElseThrow(() -> new SpotException(StatusCode.NOT_FOUND_SPOT));

        boolean isBookmarked = spot.getSpotBookmarkList().stream()
                .anyMatch(it -> it.getUser().getId().equals(currentUserId));
        boolean isAuthor = spot.getUser().getId().equals(currentUserId);

        return SpotDetailResponse.of(spot, isBookmarked, isAuthor);
    }

    // 내가 쓴 스팟 리스트 조회
    // 스팟 상세 조회
    // 스팟 북마크
    @Transactional
    public BookmarkResponse bookmark(Long currentUserId, Long spotId) {
        User user = userService.getUser(currentUserId);
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new SpotException(StatusCode.NOT_FOUND_SPOT));

        Optional<SpotBookmark> bookmark = user.getSpotBookmarkList().stream()
                .filter(it -> it.getSpot().equals(spot))
                .findFirst();

        boolean isBookmarked;

        if (bookmark.isEmpty()) {// 기존의 북마크 여부에 따라 등록, 취소 처리
            user.addSpotBookmark(SpotBookmark.builder()
                    .spot(spot)
                    .user(user)
                    .build());
            isBookmarked = true;
        } else {
            user.removeSpotBookmark(bookmark.get());
            isBookmarked = false;
        }

        return BookmarkResponse.of(spotId, isBookmarked);
    }
    // 스팟 생성   TODO user status check 로직 추가 (AuthService)
    // 스팟 수정
    // 스팟 삭제
}
