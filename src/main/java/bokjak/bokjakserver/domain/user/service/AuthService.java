package bokjak.bokjakserver.domain.user.service;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.config.jwt.JwtDto;
import bokjak.bokjakserver.config.jwt.JwtProvider;
import bokjak.bokjakserver.config.jwt.RefreshToken;
import bokjak.bokjakserver.config.jwt.RefreshTokenRepository;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.config.security.SecurityUtils;
import bokjak.bokjakserver.domain.ban.model.Ban;
import bokjak.bokjakserver.domain.ban.repository.BanRepository;
import bokjak.bokjakserver.domain.user.dto.AuthDto.*;
import bokjak.bokjakserver.domain.user.exeption.AuthException;
import bokjak.bokjakserver.domain.user.exeption.UserException;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.model.UserStatus;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static bokjak.bokjakserver.config.security.SecurityUtils.getCurrentUserSocialEmail;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KakaoService kakaoService;
    private final BanRepository banRepository;
    
    @Transactional
    public AuthMessage loginAccess(SocialLoginRequest socialLoginRequest) {
        OAuthSocialEmailResponse response = fetchSocialEmail(socialLoginRequest);        //id@KAKAO반환
        String socialEmail = response.socialEmail();
        Optional<User> findUser = userRepository.findBySocialEmail(socialEmail);
        //탈퇴 유저면 1년 지났는지 체크하는 로직 추가해야함
        AuthMessage loginMessage;
        if(findUser.isPresent()) {
            User user = findUser.get();
            checkBanUser(user);
            checkUserStatus(user);
            JwtDto jwtDto = login(LoginRequest.toLoginRequest(user));
            loginMessage = new AuthMessage(
                    jwtDto,
                    StatusCode.LOGIN.getMessage()
            );
        } else {
            String signToken = jwtProvider.createSignToken(socialEmail);
            SignToken signTokenResponse = new SignToken(signToken);
            log.info("signtokenResponse={}", signTokenResponse.signToken());
            throw new AuthException(StatusCode.NEED_TO_SIGNUP, signTokenResponse);
        }
        return loginMessage;
    }

    public JwtDto login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();
        return jwtProvider.issue(user);
    }

    @Transactional
    public SignAuthMessage signUp(SignUpRequest signUpRequest) {
        String signToken = jwtProvider.resolveSignToken(signUpRequest.signToken());
        if (!jwtProvider.validate(signToken))
            throw new AuthException(StatusCode.SIGNUP_TOKEN_ERROR);

        SigningUser signKey = jwtProvider.getSignKey(signToken);
        String socialEmail = signKey.socialEmail();
        String socialType = signKey.socialType();
        String nickname = signUpRequest.nickname();
        String profileImageUrl = signUpRequest.profileImageUrl();

        checkDuplicationNickName(nickname);
        User user = signUpRequest.toUser(socialEmail, socialType, passwordEncoder);
        userRepository.save(user);

        JwtDto jwtDto = login(LoginRequest.toLoginRequest(user));
        return new SignAuthMessage(
                jwtDto,
                StatusCode.SIGNUP_COMPLETE.getMessage()
        );
    }


    private void checkDuplicationNickName(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new UserException(StatusCode.NICKNAME_DUPLICATION);
        }
    }

    @Transactional
    public void checkBanUser(User user) {
        Optional<Ban> banedUser = banRepository.findByUserAndIsBannedIsTrue(user);
        if (banedUser.isEmpty()) return;
        boolean isBanExpired = LocalDateTime.now().isAfter(banedUser.get().getBanEndedAt());
        if (isBanExpired) banedUser.get().changeIsBanned(false);     //시간나면 바꾸기..
        else throw new UserException(StatusCode.BANNED_USER);
    }

    public static void checkUserStatus(User user) {
        UserStatus userStatus = user.getUserStatus();
        //NORMAL,BANNED,PERMANENTLY_BANED,DELETED
        switch (userStatus) {
            case NORMAL:
                break;
            case PERMANENTLY_BANED:
                throw new UserException(StatusCode.PERMANENTLY_BANNED_USER);
                /**
                 * 나머지는 todo....
                 */
        }
    }

    private OAuthSocialEmailResponse fetchSocialEmail(SocialLoginRequest socialLoginRequest) {
        String provider = socialLoginRequest.socialType();
        if (provider.equalsIgnoreCase("Kakao")) {
            return kakaoService.getKakaoId(socialLoginRequest.oauthAccessToken());
        } else {
            throw new AuthException(StatusCode.SOCIAL_TYPE_ERROR);
        }
    }

    @Transactional
    public JwtDto reissue(ReissueRequest reissueRequest) {
        return jwtProvider.reissue(reissueRequest.refreshToken());
    }

    @Transactional
    public LogoutResponse logout() {
        User user = userRepository.findBySocialEmail(getCurrentUserSocialEmail())
                .orElseThrow(() -> new UserException(StatusCode.NOT_FOUND_USER));
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new AuthException(StatusCode.NOT_FOUND_REFRESH_TOKEN));
        refreshTokenRepository.delete(refreshToken);
        refreshTokenRepository.flush();

        return LogoutResponse.of(refreshTokenRepository.existsByUser(user));
    }

}
