package bokjak.bokjakserver.domain.user.controller;


import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.user.dto.AuthDto.AuthMessage;
import bokjak.bokjakserver.domain.user.dto.UserDto.*;
import bokjak.bokjakserver.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static bokjak.bokjakserver.common.constant.SwaggerConstants.*;
import static bokjak.bokjakserver.common.dto.ApiResponse.success;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = TAG_USER, description = TAG_USER_DESCRIPTION)
public class UserController {

    private final UserService userService;

    @Operation(summary = USER_ME)
    @SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(userService.getUserInfo(principalDetails.getUserId()));
    }

    @Operation(summary = USER_CHECK_NICKNAME, description = USER_CHECK_NICKNAME_DESCRIPTION)
    @GetMapping("/check/nickname/{nickname}")
    public ApiResponse<NicknameResponse> isDuplicateNickname(@PathVariable String nickname) {
        return success(userService.isDuplicateNickname(nickname));
    }

    @Operation(summary = USER_HIDE)
    @SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
    @PostMapping("/hide")
    public ApiResponse<HideResponse> hideUser(@RequestBody @Valid HideRequest hideRequest,
                                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(userService.hideUser(hideRequest, principalDetails.getUserId()));
    }

    @Operation(summary = USER_UPDATE_PROFILE, description = USER_UPDATE_PROFILE_DESCRIPTION)
    @SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
    @PostMapping("/me/profile")
    public ApiResponse<UserInfoResponse> updateUserInfo(@RequestBody @Valid UpdateUserInfoRequest updateUserInfoRequest,
                                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(userService.updateUserInfo(updateUserInfoRequest, principalDetails.getUserId()));
    }

    @Operation(summary = USER_REVOKE)
    @SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
    @PostMapping("/revoke")
    public ApiResponse<?> revoke(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        AuthMessage revoke = userService.revoke(principalDetails.getUserId());
        return success(revoke.detailData());
    }

}
