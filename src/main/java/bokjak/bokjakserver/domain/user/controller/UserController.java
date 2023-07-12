package bokjak.bokjakserver.domain.user.controller;


import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.domain.user.dto.AuthDto.AuthMessage;
import bokjak.bokjakserver.domain.user.dto.UserDto;
import bokjak.bokjakserver.domain.user.dto.UserDto.*;
import bokjak.bokjakserver.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static bokjak.bokjakserver.common.dto.ApiResponse.success;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> getUserInfo() {
        return success(userService.getUserInfo());
    }

    @GetMapping("/check/nickname/{nickname}")
    public ApiResponse<NicknameResponse> isDuplicateNickname(@PathVariable String nickname) {
        return success(userService.isDuplicateNickname(nickname));
    }

    @PostMapping("/hide")
    public ApiResponse<HideResponse> hideUser(@RequestBody HideRequest hideRequest) {
        return success(userService.hideUser(hideRequest));
    }

    @PostMapping("/me/profile")
    public ApiResponse<UserInfoResponse> updateUserInfo(@RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
        return success(userService.updateUserInfo(updateUserInfoRequest));
    }

    @PostMapping("/revoke")
    public ApiResponse<?> revoke() {
        AuthMessage revoke = userService.revoke();
        return success(revoke.detailData());
    }

}
