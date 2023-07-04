package bokjak.bokjakserver.domain.user.controller;

import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.config.jwt.JwtDto;
import bokjak.bokjakserver.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static bokjak.bokjakserver.common.dto.ApiResponse.success;
import static bokjak.bokjakserver.domain.user.dto.AuthDto.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "Oauth2 관련 API")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody SocialLoginRequest socialLoginRequest) {
        AuthMessage authMessage = authService.loginAccess(socialLoginRequest);
        return success(authMessage.detailData());
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ApiResponse<JwtDto> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        SignAuthMessage signAuthMessage = authService.signUp(signUpRequest);
        return success(signAuthMessage.detaildata());
    }

    @Operation(summary = "토큰 재발행")
    @PostMapping("/reissue")
    public ApiResponse<JwtDto> reissue(@RequestBody ReissueRequest reissueRequest) {
        return success(authService.reissue(reissueRequest));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ApiResponse<LogoutResponse> logout() {
        return success(authService.logout());
    }
}
