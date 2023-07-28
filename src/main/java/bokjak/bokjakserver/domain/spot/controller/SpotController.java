package bokjak.bokjakserver.domain.spot.controller;

import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.spot.dto.SpotDto.*;
import bokjak.bokjakserver.domain.spot.service.SpotService;
import bokjak.bokjakserver.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
@Tag(name = SwaggerConstants.TAG_SPOT, description = SwaggerConstants.TAG_SPOT_DESCRIPTION)
public class SpotController {
    private final SpotService spotService;
    private final AuthService authService;

    @GetMapping("/{locationId}/spots")
    @Operation(summary = SwaggerConstants.SPOT_GET_ALL_BY_LOCATION, description = SwaggerConstants.SPOT_GET_ALL_BY_LOCATION_DESCRIPTION)
    public ApiResponse<PageResponse<SpotCardResponse>> getSpotsByLocation(
            @PathVariable Long locationId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false) List<Long> categoryIds
    ) {
        PageResponse<SpotCardResponse> pageResponse = spotService.getSpotsByLocationAndCategoriesExceptBlockedAuthors(principalDetails.getUserId(), pageable, cursorId, locationId, categoryIds);
        return success(pageResponse);
    }

    @GetMapping("/spots")
    @Operation(summary = SwaggerConstants.SPOT_GET_ALL_SIMPLE, description = SwaggerConstants.SPOT_GET_ALL_SIMPLE_DESCRIPTION)
    public ApiResponse<PageResponse<SpotCardResponse>> getSpots(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false) List<Long> categoryIds
    ) {
        PageResponse<SpotCardResponse> pageResponse = spotService.getSpotsByCategoriesExceptBlockedAuthors(principalDetails.getUserId(), pageable, cursorId, categoryIds);
        return success(pageResponse);
    }

    @GetMapping("/spots/bookmarks/me")
    @Operation(summary = SwaggerConstants.SPOT_GET_BOOKMARKED, description = SwaggerConstants.SPOT_GET_BOOKMARKED_DESCRIPTION)
    public ApiResponse<PageResponse<SpotCardResponse>> getMyBookmarkedSpots(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        PageResponse<SpotCardResponse> pageResponse = spotService.getMyBookmarkedSpots(principalDetails.getUserId(), pageable, cursorId);
        return success(pageResponse);
    }

    @GetMapping("/spots/me")
    @Operation(summary = SwaggerConstants.SPOT_GET_MY, description = SwaggerConstants.SPOT_GET_MY_DESCRIPTION)
    public ApiResponse<PageResponse<SpotCardResponse>> getMySpots(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        PageResponse<SpotCardResponse> pageResponse = spotService.getMySpots(principalDetails.getUserId(), pageable, cursorId);
        return success(pageResponse);
    }

    @GetMapping("/spots/me/commented")
    @Operation(summary = SwaggerConstants.SPOT_GET_COMMENTED, description = SwaggerConstants.SPOT_GET_COMMENTED_DESCRIPTION)
    public ApiResponse<PageResponse<SpotCardResponse>> getCommentedSpotsByMe(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId
    ) {
        PageResponse<SpotCardResponse> pageResponse = spotService.getSpotsCommentedByMeExceptBlockedAuthors(principalDetails.getUserId(), pageable, cursorId);
        return success(pageResponse);
    }

    @GetMapping("/spots/{spotId}")
    @Operation(summary = SwaggerConstants.SPOT_GET_DETAIL, description = SwaggerConstants.SPOT_GET_DETAIL_DESCRIPTION)
    public ApiResponse<SpotDetailResponse> getSpotDetail(
            @PathVariable Long spotId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        SpotDetailResponse response = spotService.getSpotDetail(principalDetails.getUserId(), spotId);
        return success(response);
    }

    @PostMapping("/spots/{spotId}/bookmarks")
    @Operation(summary = SwaggerConstants.SPOT_BOOKMARK, description = SwaggerConstants.SPOT_BOOKMARK_DESCRIPTION)
    public ApiResponse<BookmarkResponse> bookmark(
            @PathVariable Long spotId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        BookmarkResponse response = spotService.bookmark(principalDetails.getUserId(), spotId);
        return success(response);
    }

    @PostMapping("/{locationId}/spot-categories/{spotCategoryId}/spots")
    @Operation(summary = SwaggerConstants.SPOT_CREATE, description = SwaggerConstants.SPOT_CREATE_DESCRIPTION)
    public ApiResponse<SpotDetailResponse> createSpot(
            @PathVariable Long locationId,
            @PathVariable Long spotCategoryId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody CreateSpotRequest createSpotRequest
    ) {
        authService.checkIsBannedUser(principalDetails.getUser());
        SpotDetailResponse response = spotService.createSpot(principalDetails.getUserId(), locationId, spotCategoryId, createSpotRequest);
        return success(response);
    }

    @PutMapping("/spots/{spotId}")
    @Operation(summary = SwaggerConstants.SPOT_UPDATE, description = SwaggerConstants.SPOT_UPDATE_DESCRIPTION)
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
    @Operation(summary = SwaggerConstants.SPOT_DELETE, description = SwaggerConstants.SPOT_DELETE_DESCRIPTION)
    public ApiResponse<SpotMessage> deleteSpot(
            @PathVariable Long spotId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        authService.checkIsBannedUser(principalDetails.getUser());
        SpotMessage message = spotService.deleteSpot(principalDetails.getUserId(), spotId);
        return success(message);
    }
}
