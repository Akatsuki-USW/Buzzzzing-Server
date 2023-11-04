package bokjak.bokjakserver.domain.location.service;

import bokjak.bokjakserver.common.constant.GlobalConstants;
import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.bookmark.model.LocationBookmark;
import bokjak.bokjakserver.domain.bookmark.repository.LocationBookmarkRepository;
import bokjak.bokjakserver.domain.congestion.dto.CongestionDto.CongestionPrediction;
import bokjak.bokjakserver.domain.congestion.dto.CongestionDto.DailyCongestionStatisticResponse;
import bokjak.bokjakserver.domain.congestion.exception.CongestionException;
import bokjak.bokjakserver.domain.congestion.model.Congestion;
import bokjak.bokjakserver.domain.congestion.model.CongestionLevel;
import bokjak.bokjakserver.domain.congestion.model.DailyCongestionStatistic;
import bokjak.bokjakserver.domain.congestion.repository.CongestionRepository;
import bokjak.bokjakserver.domain.congestion.repository.DailyCongestionStatisticRepository;
import bokjak.bokjakserver.domain.location.dto.LocationDto.BookmarkResponse;
import bokjak.bokjakserver.domain.location.dto.LocationDto.LocationCardResponse;
import bokjak.bokjakserver.domain.location.dto.LocationDto.LocationDetailResponse;
import bokjak.bokjakserver.domain.location.dto.LocationDto.LocationSimpleCardResponse;
import bokjak.bokjakserver.domain.location.exception.LocationException;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.domain.location.repository.LocationRepository;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.service.UserService;
import bokjak.bokjakserver.util.CustomDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static bokjak.bokjakserver.domain.congestion.model.CongestionLevel.RELAX;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LocationService {
    private final UserService userService;
    private final LocationRepository locationRepository;
    private final CongestionRepository congestionRepository;
    private final DailyCongestionStatisticRepository dailyCongestionStatisticRepository;
    private final LocationBookmarkRepository locationBookmarkRepository;

    // 로케이션 혼잡도 기반 검색
    public PageResponse<LocationCardResponse> search(
            Pageable pageable,
            Long cursorId,
            String keyword,
            List<Long> categoryIds,
            String congestionLevelSortOrder,
            Integer cursorCongestionLevel
    ) {
        User currentUser = userService.getCurrentUser();
        Page<Location> resultPage = locationRepository.search(
                pageable,
                cursorId,
                keyword,
                categoryIds,
                // TODO: 커스텀 커서
                CongestionLevel.toSortOrder(CongestionLevel.toEnum(congestionLevelSortOrder)),
                CongestionLevel.toEnum(cursorCongestionLevel)
        );

        return makeLocationCardPageResponse(currentUser, resultPage);
    }

    // 로케이션 단순 리스트 조회
    public PageResponse<LocationSimpleCardResponse> getLocations(Pageable pageable, Long cursorId) {
        Page<Location> locations = locationRepository.getLocations(pageable, cursorId);

        Page<LocationSimpleCardResponse> responsePage = locations.map(LocationSimpleCardResponse::of);

        return PageResponse.of(responsePage);
    }

    // 혼잡도 낮은순 TOP N 조회 : 주간 통계 기반 정렬
    public PageResponse<LocationCardResponse> getTopRelaxingLocations(Pageable pageable) {
        User currentUser = userService.getCurrentUser();

        LocalDateTime start = CustomDateUtils.makePastWeekDayDateTime(Calendar.MONDAY, LocalTime.MIN);// 월요일 00시 00
        LocalDateTime end = CustomDateUtils.makePastWeekDayDateTime(Calendar.SUNDAY, LocalTime.MAX);  // 일요일 24시 59

        Page<Location> resultPage = locationRepository.getTopOfWeeklyAverageCongestion(pageable, start, end);

        return makeLocationCardPageResponse(currentUser, resultPage);
    }

    // 내가 북마크한 로케이션 리스트 조회
    public PageResponse<LocationCardResponse> getMyBookmarkedLocations(Pageable pageable, Long cursorId) {
        User currentUser = userService.getCurrentUser();
        Page<Location> resultPage = locationRepository.getBookmarked(pageable, cursorId, currentUser.getId());

        return makeLocationCardPageResponse(currentUser, resultPage);
    }

    // 상세 조회
    public LocationDetailResponse getLocationDetail(Long locationId) {
        User currentUser = userService.getCurrentUser();

        Location location = locationRepository.getLocation(locationId)
                .orElseThrow(() -> new LocationException(StatusCode.NOT_FOUND_LOCATION));
        Congestion congestion = congestionRepository.findTopByLocationIdOrderByObservedAtDesc(locationId)
                .orElseThrow(() -> new CongestionException(StatusCode.NOT_FOUND_CONGESTION));
        boolean isBookmarked = currentUser.getLocationBookmarkList().stream()
                .anyMatch(locationBookmark -> locationBookmark.getLocation().equals(location));

        CongestionPrediction congestionPrediction = makeCongestionPrediction(locationId, congestion);

        return LocationDetailResponse.of(location, congestion, congestionPrediction, isBookmarked);
    }

    // 일간 혼잡도 통계 조회
    public DailyCongestionStatisticResponse getDailyStatistics(Long locationId, String dateParam) {
        LocalDate predictionBasisDate = LocalDate.parse(dateParam, DateTimeFormatter.ofPattern(GlobalConstants.DATE_FORMAT));

        LocalDateTime start = ZonedDateTime.of(predictionBasisDate, LocalTime.MIN, ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = ZonedDateTime.of(predictionBasisDate, LocalTime.MAX, ZoneId.systemDefault()).toLocalDateTime();

        DailyCongestionStatistic dailyCongestionStatistic = dailyCongestionStatisticRepository
                .findTop1ByLocationIdAndCreatedAtBetweenOrderByCreatedAtDesc(locationId, start, end)
                .orElseThrow(() -> new CongestionException(StatusCode.NOT_FOUND_DAILY_CONGESTION_STAT));


        return DailyCongestionStatisticResponse.of(dailyCongestionStatistic);
    }

    // 북마크 등록 OR 수정
    @Transactional
    public BookmarkResponse bookmark(Long locationId, Long userId) {
        User user = userService.getUser(userId);
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationException(StatusCode.NOT_FOUND_LOCATION));

        Optional<LocationBookmark> bookmark = user.getLocationBookmarkList().stream()
                .filter(it -> it.getLocation().equals(location))
                .findFirst();

        boolean isBookmarked;

        if (bookmark.isEmpty()) {// 기존의 북마크 여부에 따라 등록, 취소 처리
            user.addLocationBookmark(LocationBookmark.builder()
                    .user(user)
                    .location(location)
                    .build());
            isBookmarked = true;
        } else {
            user.removeLocationBookmark(bookmark.get());
            isBookmarked = false;
        }

        return BookmarkResponse.of(locationId, isBookmarked);
    }

    /* private 함수 */

    private PageResponse<LocationCardResponse> makeLocationCardPageResponse(User currentUser, Page<Location> resultPage) {
        List<Long> bookmarkedLocationIdList = locationBookmarkRepository.findAllByUser(currentUser).stream()
                .map(it -> it.getLocation().getId()).toList();
        Page<LocationCardResponse> responsePage = resultPage
                .map(it -> LocationCardResponse.of(it, bookmarkedLocationIdList.contains(it.getId())));

        return PageResponse.of(responsePage);
    }

    // 혼잡도 예측
    private CongestionPrediction makeCongestionPrediction(Long locationId, Congestion congestion) {
        CongestionPrediction congestionPrediction = new CongestionPrediction();

        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() < GlobalConstants.CONGESTION_STATISTIC_START_TIME)// 현재 9시 이전일 경우 함수 종료
            return congestionPrediction;

        // 저번주 오늘의 혼잡도 통계로 오늘 앞으로의 혼잡도 양상을 예측
        LocalDate predictionBasisDate = now.minusWeeks(GlobalConstants.CONGESTION_PREDICTION_WEEK).toLocalDate();
        LocalDateTime start = ZonedDateTime.of(predictionBasisDate, LocalTime.MIN, ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = ZonedDateTime.of(predictionBasisDate, LocalTime.MAX, ZoneId.systemDefault()).toLocalDateTime();

        DailyCongestionStatistic dailyCongestionStatistic = dailyCongestionStatisticRepository
                .findTop1ByLocationIdAndCreatedAtBetweenOrderByCreatedAtDesc(locationId, start, end)
                .orElseThrow(() -> new CongestionException(StatusCode.NOT_FOUND_DAILY_CONGESTION_STAT));

        // 현재 시각 이후의 데이터만 추출
        List<Map<String, Integer>> data = dailyCongestionStatistic.getContent().get(GlobalConstants.CONTENT_DATA)
                .stream().filter(it -> it.get(GlobalConstants.CONTENT_HOUR) > now.getHour()).toList();

        if (congestion.getCongestionLevel() == RELAX.getValue()) {// 현재 여유인 경우 : 몇시까지 여유로울지 + 몇시에 혼잡해질지
            for (Map<String, Integer> row : data) {
                int hour = row.get(GlobalConstants.CONTENT_HOUR), level = row.get(GlobalConstants.CONTENT_CONGESTION_LEVEL);
                if (level != RELAX.getValue()) {
                    congestionPrediction.setMayBuzzAt(hour);
                    break;
                } else congestionPrediction.setMayRelaxUntil(hour);// 시각 갱신
            }
        } else {// 현재 보통 혹은 혼잡인 경우 : 몇시까지 혼잡할지 + 몇시에 여유로워질지
            for (Map<String, Integer> row : data) {
                int hour = row.get(GlobalConstants.CONTENT_HOUR), level = row.get(GlobalConstants.CONTENT_CONGESTION_LEVEL);
                if (level == RELAX.getValue()) {
                    congestionPrediction.setMayRelaxAt(hour);
                    break;
                } else congestionPrediction.setMayBuzzUntil(hour);
            }
        }

        return congestionPrediction;
    }
}
