package bokjak.bokjakserver.domain.spot.controller;

import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.spot.dto.SpotDto.*;
import bokjak.bokjakserver.domain.spot.service.SpotService;
import bokjak.bokjakserver.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static bokjak.bokjakserver.common.dto.ApiResponse.success;

@Slf4j
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class SpotController {
    private final SpotService spotService;
    private final AuthService authService;

    @GetMapping("/{locationId}/spots")
    public ApiResponse<PageResponse<SpotCardResponse>> getSpots(
            @PathVariable Long locationId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false) List<Long> categoryIds
    ) {
        PageResponse<SpotCardResponse> pageResponse = spotService.getSpots(principalDetails.getUserId(), pageable, cursorId, locationId, categoryIds);
        return success(pageResponse);
    }

    @GetMapping("/spots/bookmarks/me")
    public ApiResponse<PageResponse<SpotCardResponse>> getMyBookmarkedSpots(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        PageResponse<SpotCardResponse> pageResponse = spotService.getMyBookmarkedSpots(principalDetails.getUserId(), pageable, cursorId);
        return success(pageResponse);
    }

    @GetMapping("/spots/me")
    public ApiResponse<PageResponse<SpotCardResponse>> getMySpots(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        PageResponse<SpotCardResponse> pageResponse = spotService.getMySpots(principalDetails.getUserId(), pageable, cursorId);
        return success(pageResponse);
    }

    @GetMapping("/spots/{spotId}")
    public ApiResponse<SpotDetailResponse> getSpotDetail(
            @PathVariable Long spotId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        SpotDetailResponse response = spotService.getSpotDetail(principalDetails.getUserId(), spotId);
        return success(response);
    }

    @PostMapping("/spots/{spotId}/bookmarks")
    public ApiResponse<BookmarkResponse> bookmark(
            @PathVariable Long spotId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        BookmarkResponse response = spotService.bookmark(principalDetails.getUserId(), spotId);
        return success(response);
    }

    @PostMapping("/spots")
    public ApiResponse<SpotDetailResponse> createSpot(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody CreateSpotRequest createSpotRequest
    ) {
        authService.checkIsBannedUser(principalDetails.getUser());
        SpotDetailResponse response = spotService.createSpot(principalDetails.getUserId(), createSpotRequest);
        return success(response);
    }

    @PutMapping("/spots/{spotId}")
    public ApiResponse<SpotDetailResponse> updateSpot(
            @PathVariable Long spotId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody UpdateSpotRequest updateSpotRequest
    ) {
        authService.checkIsBannedUser(principalDetails.getUser());
        SpotDetailResponse response = spotService.updateSpot(principalDetails.getUserId(), spotId, updateSpotRequest);
        return success(response);
    }

    @DeleteMapping("/spots/{spotId}")
    public ApiResponse<SpotMessage> deleteSpot(
            @PathVariable Long spotId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        authService.checkIsBannedUser(principalDetails.getUser());
        SpotMessage message = spotService.deleteSpot(principalDetails.getUserId(), spotId);
        return success(message);
    }
}
