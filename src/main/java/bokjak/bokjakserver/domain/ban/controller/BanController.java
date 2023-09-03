package bokjak.bokjakserver.domain.ban.controller;

import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.ban.dto.BanDto.BanListResponse;
import bokjak.bokjakserver.domain.ban.service.BanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static bokjak.bokjakserver.common.constant.SwaggerConstants.*;
import static bokjak.bokjakserver.common.dto.ApiResponse.success;

@RestController
@RequestMapping("/bans")
@RequiredArgsConstructor
@Tag(name = TAG_BAN, description = TAG_BAN_DESCRIPTION)
public class BanController {

    private final BanService banService;

    @GetMapping("/me")
    @Operation(summary = BAN_ME)
    @SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
    public ApiResponse<BanListResponse> getMyBan(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(banService.getMyBanList(principalDetails.getUserId()));
    }

}
