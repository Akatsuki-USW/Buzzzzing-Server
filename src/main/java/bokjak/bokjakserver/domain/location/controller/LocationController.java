package bokjak.bokjakserver.domain.location.controller;

import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.common.dto.PageWithLastModified;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.congestion.dto.CongestionDto.DailyCongestionStatisticResponse;
import bokjak.bokjakserver.domain.location.dto.LocationDto.BookmarkResponse;
import bokjak.bokjakserver.domain.location.dto.LocationDto.LocationCardResponse;
import bokjak.bokjakserver.domain.location.dto.LocationDto.LocationDetailResponse;
import bokjak.bokjakserver.domain.location.dto.LocationDto.LocationSimpleCardResponse;
import bokjak.bokjakserver.domain.location.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static bokjak.bokjakserver.common.constant.GlobalConstants.TOP_LOCATIONS_SIZE;
import static bokjak.bokjakserver.common.dto.ApiResponse.success;

@Slf4j
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
@SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
@Tag(name = SwaggerConstants.TAG_LOCATION, description = SwaggerConstants.TAG_LOCATION_DESCRIPTION)
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    @Operation(summary = SwaggerConstants.LOCATION_SEARCH, description = SwaggerConstants.LOCATION_SEARCH_DESCRIPTION)
    public ApiResponse<PageResponse<LocationCardResponse>> getLocationsBasedOnCongestion(
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

    @GetMapping("/simple")
    @Operation(summary = SwaggerConstants.LOCATION_GET_ALL, description = SwaggerConstants.LOCATION_GET_ALL_DESCRIPTION)
    public ResponseEntity<ApiResponse<PageResponse<LocationSimpleCardResponse>>> getSimpleLocations(
            ServletWebRequest request,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        PageWithLastModified<LocationSimpleCardResponse> result = locationService.getSimpleLocations(pageable, cursorId);
        ZonedDateTime lastModifiedAt = ZonedDateTime.of(result.getLastModified(), ZoneId.systemDefault());

        // Header의 If-Modified-Since와 같다면 304 응답
        if (request.checkNotModified(lastModifiedAt.toInstant().toEpochMilli())) {
            return null;
        }

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .lastModified(lastModifiedAt)   // HTTP Cache: Last-Modified
                .body(ApiResponse.success(result.getPageResponse()));
    }

    @GetMapping("/top")
    @Operation(summary = SwaggerConstants.LOCATION_GET_TOP, description = SwaggerConstants.LOCATION_GET_TOP_DESCRIPTION)
    public ApiResponse<PageResponse<LocationCardResponse>> getTopRelaxingLocations(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = TOP_LOCATIONS_SIZE) Pageable pageable
    ) {
        PageResponse<LocationCardResponse> pageResponse = locationService.getTopRelaxingLocations(pageable);
        return success(pageResponse);
    }

    @GetMapping("/{locationId}")
    @Operation(summary = SwaggerConstants.LOCATION_DETAIL, description = SwaggerConstants.LOCATION_DETAIL_DESCRIPTION)
    public ApiResponse<LocationDetailResponse> getLocation(@PathVariable Long locationId) {
        LocationDetailResponse response = locationService.getLocationDetail(locationId);
        return success(response);
    }

    @GetMapping("/{locationId}/congestion/daily")
    @Operation(summary = SwaggerConstants.LOCATION_CONGESTION_DAILY, description = SwaggerConstants.LOCATION_CONGESTION_DAILY_DESCRIPTION)
    public ApiResponse<DailyCongestionStatisticResponse> getDailyStatistics(
            @PathVariable Long locationId,
            @RequestParam String date
    ) {
        DailyCongestionStatisticResponse response = locationService.getDailyStatistics(locationId, date);
        return success(response);
    }

    @GetMapping("/bookmarks/me")
    @Operation(summary = SwaggerConstants.LOCATION_BOOKMARKED, description = SwaggerConstants.LOCATION_BOOKMARKED_DESCRIPTION)
    public ApiResponse<PageResponse<LocationCardResponse>> getMyBookmarkedLocations(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        PageResponse<LocationCardResponse> pageResponse = locationService.getMyBookmarkedLocations(pageable, cursorId);
        return success(pageResponse);
    }

    @PostMapping("/{locationId}/bookmarks")
    @Operation(summary = SwaggerConstants.LOCATION_BOOKMARK, description = SwaggerConstants.LOCATION_BOOKMARK_DESCRIPTION)
    public ApiResponse<BookmarkResponse> bookmark(@PathVariable Long locationId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        BookmarkResponse response = locationService.bookmark(locationId, principalDetails.getUserId());
        return success(response);
    }
}
