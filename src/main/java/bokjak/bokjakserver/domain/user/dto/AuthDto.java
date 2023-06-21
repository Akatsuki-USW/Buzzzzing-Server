package bokjak.bokjakserver.domain.user.dto;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.config.jwt.JwtDto;
import bokjak.bokjakserver.domain.user.exeption.UserException;
import bokjak.bokjakserver.domain.user.model.Role;
import bokjak.bokjakserver.domain.user.model.SocialType;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.model.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class AuthDto {

    public record SocialLoginRequest(
            @Schema(description = "소셜 타입", example = "KAKAO")
            String socialType,
            @Schema(description = "Oauth2 Access 토큰", example = "ya29.a0Aa4xrXNXkiDBMm7MtSneVejzvup")
            String oauthAccessToken
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
            String signToken,
            String nickname,
            String email,
            String profileImageUrl
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
                    .profileImageUrl(profileImageUrl).build();
        }
    }

    public record SigningUser(String socialEmail, String socialUuid, String socialType) {}
    public record ReissueRequest(String refreshToken) {}


    public record AuthMessage(Object detailData, String detailMessage) {}

    public record SignAuthMessage(JwtDto detaildata, String detailMessage){}

    public record SignToken(String signToken) {}

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
