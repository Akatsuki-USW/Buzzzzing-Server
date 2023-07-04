package bokjak.bokjakserver.domain.user.service;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.config.jwt.JwtDto;
import bokjak.bokjakserver.config.jwt.JwtProvider;
import bokjak.bokjakserver.config.jwt.RefreshToken;
import bokjak.bokjakserver.config.jwt.RefreshTokenRepository;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.ban.model.Ban;
import bokjak.bokjakserver.domain.ban.repository.BanRepository;
import bokjak.bokjakserver.domain.user.dto.AuthDto.*;
import bokjak.bokjakserver.domain.user.exeption.AuthException;
import bokjak.bokjakserver.domain.user.exeption.UserException;
import bokjak.bokjakserver.domain.user.model.RevokeUser;
import bokjak.bokjakserver.domain.user.model.SleepingUser;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.model.UserStatus;
import bokjak.bokjakserver.domain.user.repository.RevokeUserRepository;
import bokjak.bokjakserver.domain.user.repository.SleepingUserRepository;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import bokjak.bokjakserver.util.CustomEncryptUtil;
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
    private final UserService userService;
    private final KakaoService kakaoService;
    private final BanRepository banRepository;
    private final SleepingUserRepository sleepingUserRepository;
    private final RevokeUserRepository revokeUserRepository;
    private final CustomEncryptUtil customEncryptUtil;

    @Transactional(noRollbackFor = {AuthException.class})
    public AuthMessage loginAccess(SocialLoginRequest socialLoginRequest) {
        OAuthSocialEmailResponse response = fetchSocialEmail(socialLoginRequest);
        String socialEmail = response.socialEmail();

        checkIsSleepingUser(socialEmail);
        Optional<User> findUser = userRepository.findBySocialEmail(socialEmail);
        checkIsRevokeUser(socialEmail);
        AuthMessage loginMessage;
        if(findUser.isPresent()) {
            User user = findUser.get();
            checkUserStatus(user);
            user.updateLastLoginDate();

            JwtDto jwtDto = login(LoginRequest.toLoginRequest(user));
            loginMessage = new AuthMessage(
                    jwtDto,
                    StatusCode.LOGIN.getMessage()
            );
        } else {
            String signToken = jwtProvider.createSignToken(socialEmail);
            SignToken signTokenResponse = new SignToken(signToken);
            log.info("signTokenResponse={}", signTokenResponse.signToken());
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
        userService.validateDuplicateNickname(nickname);
    }

    @Transactional
    public void checkIsSleepingUser(String socialEmail) {
        Optional<SleepingUser> sleepingUser = sleepingUserRepository.findBySocialEmail(socialEmail);
        if (sleepingUser.isEmpty()) return;
        User user = userRepository.findById(sleepingUser.get().getOriginalId()).orElseThrow(() -> new UserException(StatusCode.NOT_FOUND_USER));
        user.changeWakeUpUser(sleepingUser.get());

        sleepingUserRepository.delete(sleepingUser.get());
    }

    @Transactional
    public void checkBanUser(User user) {
        Optional<Ban> banedUser = banRepository.findByUserAndIsBannedIsTrue(user);
        if (banedUser.isEmpty()) return;
        boolean isBanExpired = LocalDateTime.now().isAfter(banedUser.get().getBanEndedAt());
        if (isBanExpired) {
            banedUser.get().changeIsBanned(false);
            user.updateUserStatus(UserStatus.NORMAL);
        }
    }

    @Transactional
    public void checkIsRevokeUser(String socialEmail) {
        Optional<RevokeUser> checkUser = revokeUserRepository.findBySocialEmail(customEncryptUtil.hash(socialEmail));
        if (checkUser.isEmpty()) return;
        RevokeUser revokeUser = checkUser.get();
        boolean isRevokedExpired = LocalDateTime.now().isAfter(revokeUser.getRevokedAt().plusMonths(1));
        if (isRevokedExpired) {
            revokeUser.deleteRevokeUser();
        }
        else throw new UserException(StatusCode.REVOKE_USER);
    }

    @Transactional
    public void checkUserStatus(User user) {
        UserStatus userStatus = user.getUserStatus();

        switch (userStatus) {
            case NORMAL:
                break;
            case BANNED:
                checkBanUser(user);
                break;
            case BLACKLIST:
                checkBanUser(user);
                throw new UserException(StatusCode.BLACKLIST_BANNED_USER);
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
