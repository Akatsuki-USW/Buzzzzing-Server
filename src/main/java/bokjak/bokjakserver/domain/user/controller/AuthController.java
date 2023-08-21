package bokjak.bokjakserver.domain.user.controller;

import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.config.jwt.JwtDto;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static bokjak.bokjakserver.common.constant.SwaggerConstants.*;
import static bokjak.bokjakserver.common.dto.ApiResponse.success;
import static bokjak.bokjakserver.domain.user.dto.AuthDto.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = TAG_AUTH, description = TAG_AUTH_DESCRIPTION)
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Operation(summary = AUTH_LOGIN, description = AUTH_LOGIN_DESCRIPTION)
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody @Valid SocialLoginRequest socialLoginRequest) {
        AuthMessage authMessage = authService.loginAccess(socialLoginRequest);
        return success(authMessage.detailData());
    }

    @Operation(summary = AUTH_SIGNUP)
    @PostMapping("/signup")
    public ApiResponse<JwtDto> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        SignAuthMessage signAuthMessage = authService.signUp(signUpRequest);
        return success(signAuthMessage.detaildata());
    }

    @Operation(summary = AUTH_REISSUE)
    @PostMapping("/reissue")
    public ApiResponse<JwtDto> reissue(@RequestBody ReissueRequest reissueRequest) {
        return success(authService.reissue(reissueRequest));
    }

    @Operation(summary = AUTH_LOGOUT)
    @SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
    @PostMapping("/logout")
    public ApiResponse<LogoutResponse> logout(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(authService.logout(principalDetails.getUserId()));
    }

    @PostMapping("/login/admin")
    @Operation(summary = ADMIN_LOGIN, description = ADMIN_LOGIN_DESCRIPTION)
    public ApiResponse<?> adminLogin(@RequestBody @Valid AdminLoginRequest adminLoginRequest) {
        AuthMessage authMessage = authService.loginAdmin(adminLoginRequest);
        return success(authMessage.detailData());
    }


}
