package bokjak.bokjakserver.domain.user.controller;


import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.domain.user.dto.AuthDto.AuthMessage;
import bokjak.bokjakserver.domain.user.dto.UserDto;
import bokjak.bokjakserver.domain.user.dto.UserDto.*;
import bokjak.bokjakserver.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<UserInfoResponse> getUserInfo() {
        return success(userService.getUserInfo());
    }

    @Operation(summary = USER_CHECK_NICKNAME, description = USER_CHECK_NICKNAME_DESCRIPTION)
    @GetMapping("/check/nickname/{nickname}")
    public ApiResponse<NicknameResponse> isDuplicateNickname(@PathVariable String nickname) {
        return success(userService.isDuplicateNickname(nickname));
    }

    @Operation(summary = USER_HIDE)
    @SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
    @PostMapping("/hide")
    public ApiResponse<HideResponse> hideUser(@RequestBody HideRequest hideRequest) {
        return success(userService.hideUser(hideRequest));
    }

    @Operation(summary = USER_UPDATE_PROFILE, description = USER_UPDATE_PROFILE_DESCRIPTION)
    @SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
    @PostMapping("/me/profile")
    public ApiResponse<UserInfoResponse> updateUserInfo(@RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
        return success(userService.updateUserInfo(updateUserInfoRequest));
    }

    @Operation(summary = USER_REVOKE)
    @SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
    @PostMapping("/revoke")
    public ApiResponse<?> revoke() {
        AuthMessage revoke = userService.revoke();
        return success(revoke.detailData());
    }

}
