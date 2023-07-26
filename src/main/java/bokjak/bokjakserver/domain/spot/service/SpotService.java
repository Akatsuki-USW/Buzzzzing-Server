package bokjak.bokjakserver.domain.spot.service;

import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.bookmark.model.SpotBookmark;
import bokjak.bokjakserver.domain.bookmark.repository.SpotBookmarkRepository;
import bokjak.bokjakserver.domain.category.exception.CategoryException;
import bokjak.bokjakserver.domain.category.model.SpotCategory;
import bokjak.bokjakserver.domain.category.repository.SpotCategoryRepository;
import bokjak.bokjakserver.domain.location.exception.LocationException;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.domain.location.repository.LocationRepository;
import bokjak.bokjakserver.domain.spot.dto.SpotDto.*;
import bokjak.bokjakserver.domain.spot.exception.SpotException;
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
import java.util.Optional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SpotService {
    private final UserService userService;
    private final SpotRepository spotRepository;
    private final SpotBookmarkRepository spotBookmarkRepository;
    private final LocationRepository locationRepository;
    private final SpotCategoryRepository spotCategoryRepository;


    // 스팟 리스트 조회: 특정 로케이션
    public PageResponse<SpotCardResponse> getSpotsByLocationAndCategoriesExceptBlockedAuthors(
            Long currentUserId,
            Pageable pageable,
            Long cursorId,
            Long locationId,
            List<Long> categoryIds
    ) {
        User user = userService.getUser(currentUserId);
        Page<Spot> resultPage = spotRepository.findAllByLocationAndCategoriesExceptBlockedAuthors(user.getId(), pageable, cursorId, locationId, categoryIds);

        return makeSpotCardResponsePageResponse(user, resultPage);
    }

    // 스팟 리스트 조회: 모든 로케이션
    public PageResponse<SpotCardResponse> getSpotsByCategoriesExceptBlockedAuthors(
            Long currentUserId,
            Pageable pageable,
            Long cursorId,
            List<Long> categoryIds
    ) {
        User user = userService.getUser(currentUserId);
        Page<Spot> resultPage = spotRepository.findAllByCategoriesExceptBlockedAuthors(user.getId(), pageable, cursorId, categoryIds);

        return makeSpotCardResponsePageResponse(user, resultPage);
    }

    // 내가 북마크한 스팟 리스트 조회
    public PageResponse<SpotCardResponse> getMyBookmarkedSpots(Long currentUserId, Pageable pageable, Long cursorId) {
        User user = userService.getUser(currentUserId);
        Page<Spot> resultPage = spotRepository.findAllBookmarked(pageable, cursorId, currentUserId);

        return makeSpotCardResponsePageResponse(user, resultPage);
    }

    // 내가 쓴 스팟 리스트 조회
    public PageResponse<SpotCardResponse> getMySpots(Long currentUserId, Pageable pageable, Long cursorId) {
        User user = userService.getUser(currentUserId);
        Page<Spot> resultPage = spotRepository.findAllMy(pageable, cursorId, currentUserId);

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
        Spot spot = spotRepository.findOne(spotId)
                .orElseThrow(() -> new SpotException(StatusCode.NOT_FOUND_SPOT));

        boolean isBookmarked = isBookmarked(currentUserId, spot);
        boolean isAuthor = spot.getUser().getId().equals(currentUserId);

        return SpotDetailResponse.of(spot, isBookmarked, isAuthor);
    }

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

    // 스팟 생성
    @Transactional
    public SpotDetailResponse createSpot(Long currentUserId, CreateSpotRequest createSpotRequest) {
        User user = userService.getUser(currentUserId);
        Location location = locationRepository.findById(createSpotRequest.locationId())
                .orElseThrow(() -> new LocationException(StatusCode.NOT_FOUND_LOCATION));
        SpotCategory spotCategory = spotCategoryRepository.findById(createSpotRequest.spotCategoryId())
                .orElseThrow(() -> new CategoryException(StatusCode.NOT_FOUND_SPOT_CATEGORY));

        Spot spot = spotRepository.save(createSpotRequest.toEntity(user, location, spotCategory));

        createSpotRequest.imageUrls().forEach(imageUrl -> spot.addSpotImage(SpotImage.of(spot, imageUrl)));

        return SpotDetailResponse.of(
                spot,
                isBookmarked(currentUserId, spot),
                spot.getUser().getId().equals(currentUserId)
        );
    }

    // 스팟 수정
    @Transactional
    public SpotDetailResponse updateSpot(Long currentUserId, Long spotId, UpdateSpotRequest updateSpotRequest) {
        User user = userService.getUser(currentUserId);
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new SpotException(StatusCode.NOT_FOUND_SPOT));

        checkIsAuthor(user, spot);

        Location location = locationRepository.findById(updateSpotRequest.locationId())
                .orElseThrow(() -> new LocationException(StatusCode.NOT_FOUND_LOCATION));
        SpotCategory spotCategory = spotCategoryRepository.findById(updateSpotRequest.spotCategoryId())
                .orElseThrow(() -> new CategoryException(StatusCode.NOT_FOUND_SPOT_CATEGORY));

        spot.update(
                location,
                spotCategory,
                updateSpotRequest.title(),
                updateSpotRequest.address(),
                updateSpotRequest.content(),
                updateSpotRequest.imageUrls().stream().map(it -> SpotImage.of(spot, it)).toList()
        );

        // TODO 이전의 상세 조회 응답값에서 달라진게 없다. 고민해보자 (spot_bookmark 조회 쿼리 1 낭비)
        return SpotDetailResponse.of(
                spot,
                isBookmarked(currentUserId, spot),
                spot.getUser().getId().equals(currentUserId)
        );
    }

    // 스팟 삭제
    @Transactional
    public SpotMessage deleteSpot(Long currentUserId, Long spotId) {
        User user = userService.getUser(currentUserId);
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new SpotException(StatusCode.NOT_FOUND_SPOT));

        checkIsAuthor(user, spot);

        spotRepository.delete(spot);
        return new SpotMessage(true);
    }

    private static boolean isBookmarked(Long currentUserId, Spot spot) {
        return spot.getSpotBookmarkList().stream()
                .anyMatch(it -> it.getUser().getId().equals(currentUserId));
    }

    private static void checkIsAuthor(User user, Spot spot) {// 작성자인지 확인: 수정, 삭제는 작성자만 권한을 가짐
        if (!spot.getUser().equals(user)) {
            throw new SpotException(StatusCode.NOT_AUTHOR);
        }
    }
}
