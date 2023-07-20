package bokjak.bokjakserver.domain.user.dto;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.config.jwt.JwtDto;
import bokjak.bokjakserver.domain.user.exeption.UserException;
import bokjak.bokjakserver.domain.user.model.Role;
import bokjak.bokjakserver.domain.user.model.SocialType;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.model.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Slf4j
public class AuthDto {

    public record SocialLoginRequest(
            @NotBlank
            @Schema(description = "소셜 타입", example = "KAKAO")
            String socialType,
            @NotBlank
            @Schema(description = "Oauth2 Access 토큰", example = "ya29.a0Aa4xrXNXkiDBMm7MtSneVejzvup")
            String oauthAccessToken,
            @NotBlank
            String fcmToken
    ){}

    public record LoginRequest(String socialEmail, String password) {
        @Builder
        public LoginRequest {}

        public static LoginRequest toLoginRequest(User user) {
            String socialEmail = user.getSocialEmail();
            return LoginRequest.builder()
                    .socialEmail(socialEmail)
                    .password(socialEmail+"buz")
                    .build();
        }

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(socialEmail, password);
        }
    }
    public record SignUpRequest(
            @NotBlank
            String signToken,
            @NotBlank
            String nickname,
            @Email
            String email,
            String profileImageUrl,
            @NotBlank
            String fcmToken
    ) {
        @Builder
        public SignUpRequest{}

        public SocialType getSocialType(String socialType) {
            if (socialType.equalsIgnoreCase(SocialType.KAKAO.name())) {
                return SocialType.KAKAO;
            } else throw new UserException(StatusCode.SOCIAL_TYPE_ERROR);
        }

        public User toUser(String socialEmail, String socialType, PasswordEncoder passwordEncoder) {
            return User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(socialEmail+"buz"))
                    .socialEmail(socialEmail)
                    .socialType(getSocialType(socialType))
                    .nickname(nickname)
                    .role(Role.ROLE_USER)
                    .userStatus(UserStatus.NORMAL)
                    .lastLoginDate(LocalDateTime.now())
                    .fcmToken(fcmToken)
                    .profileImageUrl(profileImageUrl).build();
        }
        public User toDummy(String email, String nickname, String socialEmail) {
            return User.builder()
                    .email(email)
                    .profileImageUrl("www.test.com")
                    .nickname(nickname)
                    .socialEmail(socialEmail)
                    .userStatus(UserStatus.NORMAL)
                    .socialType(SocialType.KAKAO)
                    .role(Role.ROLE_USER)
                    .lastLoginDate(LocalDateTime.now())
                    .build();
        }
    }

    public record SigningUser(String socialEmail, String socialUuid, String socialType) {}
    public record ReissueRequest(
            @NotBlank
            String refreshToken) {}


    public record AuthMessage(Object detailData, String detailMessage) {}

    public record SignAuthMessage(JwtDto detaildata, String detailMessage){}

    public record SignToken(String signToken) {}

    public record RevokeKakaoResponse(String id) {}

    public record OAuthSocialEmailResponse(String socialEmail){

        public static OAuthSocialEmailResponse to(String socialEmail) {
            return new OAuthSocialEmailResponse(socialEmail);
        }
    }

    public record LogoutResponse(
            @Schema(description = "로그아웃 확인", example = "true")
            boolean isLogout) {
        public static LogoutResponse of(boolean isLogout) {
            return new LogoutResponse(!isLogout);
        }
    }

}
