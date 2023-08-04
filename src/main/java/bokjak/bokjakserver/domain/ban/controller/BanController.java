package bokjak.bokjakserver.domain.ban.controller;

import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.ban.dto.BanDto;
import bokjak.bokjakserver.domain.ban.dto.BanDto.BanListResponse;
import bokjak.bokjakserver.domain.ban.service.BanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static bokjak.bokjakserver.common.dto.ApiResponse.success;

@RestController
@RequestMapping("/mypage/ban")
@RequiredArgsConstructor
public class BanController {

    private final BanService banService;

    @GetMapping("/my")
    public ApiResponse<BanListResponse> getMyBan(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(banService.getMyBanList(principalDetails.getUserId()));
    }

}
