package bokjak.bokjakserver.domain.location.controller;

import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.congestion.dto.CongestionDto.DailyCongestionStatisticResponse;
import bokjak.bokjakserver.domain.location.dto.LocationDto.BookmarkResponse;
import bokjak.bokjakserver.domain.location.dto.LocationDto.LocationCardResponse;
import bokjak.bokjakserver.domain.location.dto.LocationDto.LocationDetailResponse;
import bokjak.bokjakserver.domain.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static bokjak.bokjakserver.common.constant.GlobalConstants.TOP_LOCATIONS_SIZE;
import static bokjak.bokjakserver.common.dto.ApiResponse.success;

@Slf4j
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public ApiResponse<PageResponse<LocationCardResponse>> getLocations(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String congestionSort,
            @RequestParam(required = false) Integer cursorCongestionLevel,
            @RequestParam(required = false) List<Long> categoryIds
    ) {
        PageResponse<LocationCardResponse> pageResponse = locationService.search(pageable, cursorId, keyword, categoryIds, congestionSort, cursorCongestionLevel);
        return success(pageResponse);
    }

    @GetMapping("/top")
    public ApiResponse<PageResponse<LocationCardResponse>> getTopRelaxingLocations(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = TOP_LOCATIONS_SIZE) Pageable pageable
    ) {
        PageResponse<LocationCardResponse> pageResponse = locationService.getTopRelaxingLocations(pageable);
        return success(pageResponse);
    }

    @GetMapping("/{locationId}")
    public ApiResponse<LocationDetailResponse> getLocation(@PathVariable Long locationId) {
        LocationDetailResponse response = locationService.getLocationDetail(locationId);
        return success(response);
    }

    @GetMapping("/{locationId}/congestion/daily")
    public ApiResponse<DailyCongestionStatisticResponse> getDailyStatistics(
            @PathVariable Long locationId,
            @RequestParam String date
    ) {
        DailyCongestionStatisticResponse response = locationService.getDailyStatistics(locationId, date);
        return success(response);
    }

    @GetMapping("/bookmarks/me")
    public ApiResponse<PageResponse<LocationCardResponse>> getMyBookmarkedLocations(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        PageResponse<LocationCardResponse> pageResponse = locationService.getMyBookmarkedLocations(pageable, cursorId);
        return success(pageResponse);
    }

    @PostMapping("/{locationId}/bookmarks")
    public ApiResponse<BookmarkResponse> bookmark(@PathVariable Long locationId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        BookmarkResponse response = locationService.bookmark(locationId, principalDetails.getUserId());
        return success(response);
    }
}
