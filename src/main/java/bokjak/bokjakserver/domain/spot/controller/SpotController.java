package bokjak.bokjakserver.domain.spot.controller;

import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.common.dto.PageResponse;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.spot.dto.SpotDto.SpotCardResponse;
import bokjak.bokjakserver.domain.spot.service.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/{locationId}/spots")
    public ApiResponse<PageResponse<SpotCardResponse>> getSpots(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long locationId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false) List<Long> categoryIds
    ) {
        PageResponse<SpotCardResponse> pageResponse = spotService.getSpots(principalDetails.getUserId(), pageable, cursorId, locationId, categoryIds);
        return success(pageResponse);
    }

}
