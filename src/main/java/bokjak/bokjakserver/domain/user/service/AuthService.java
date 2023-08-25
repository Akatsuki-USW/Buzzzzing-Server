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
import bokjak.bokjakserver.domain.user.model.*;
import bokjak.bokjakserver.domain.user.repository.BlackListRepository;
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
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    private final BlackListRepository blackListRepository;

    @Transactional(noRollbackFor = {AuthException.class})
    public AuthMessage loginAccess(SocialLoginRequest socialLoginRequest) {
        OAuthSocialEmailResponse response = fetchSocialEmail(socialLoginRequest);
        String socialEmail = response.socialEmail();

        checkIsSleepingUser(socialEmail);
        Optional<User> findUser = userRepository.findBySocialEmail(socialEmail);
        checkIsRevokeUser(socialEmail);
        checkIsBlackListAndRevokeUser(socialEmail);
        AuthMessage loginMessage;
        if(findUser.isPresent()) {
            User user = findUser.get();
            checkUserStatus(user);
            user.updateLastLoginDate();
            user.updateFcmToken(socialLoginRequest.fcmToken());

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

    @Transactional
    public void checkIsBlackListAndRevokeUser(String socialEmail) {
        List<BlackList> blackLists = blackListRepository.findAll();
        Optional<BlackList> blackList = blackLists.stream()
                .filter(b -> passwordEncoder.matches(socialEmail, b.getSocialEmail()))
                .findAny();
        if (blackList.isEmpty()) return;

        else if (blackList.get().getBanEndedAt().isBefore(LocalDateTime.now())) {
            blackListRepository.delete(blackList.get());
            return;
        }

        throw new UserException(StatusCode.BLACKLIST_BANNED_USER);
    }

    public JwtDto login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();
        return jwtProvider.issue(user);
    }

    public JwtDto adminJwt(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();
        return jwtProvider.adminIssue(user);
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

        user.updateFcmToken(signUpRequest.fcmToken());
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

    // TODO Interceptor, Security .hasAuthority 등 고민. 컨트롤러 내부 로직 타기 전에 처리하고 싶다.
    public void checkIsBannedUser(User user) {  // 작성 권한 체크: BANNED 유저는 작성, 수정, 삭제 할 수 없음
        if (user.getUserStatus().equals(UserStatus.BANNED)) {
            throw new UserException(StatusCode.BANNED_USER);
        }
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
            throw new UserException(StatusCode.SOCIAL_TYPE_ERROR);
        }
    }

    @Transactional
    public JwtDto reissue(ReissueRequest reissueRequest) {
        return jwtProvider.reissue(reissueRequest.refreshToken());
    }

    @Transactional
    public LogoutResponse logout(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(StatusCode.NOT_FOUND_USER));
        user.removeFcmToken();

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new UserException(StatusCode.NOT_FOUND_REFRESH_TOKEN));
        refreshTokenRepository.delete(refreshToken);
        refreshTokenRepository.flush();

        return LogoutResponse.of(refreshTokenRepository.existsByUser(user));
    }

    @Transactional
    public AuthMessage loginAdmin(AdminLoginRequest adminLoginRequest) {
        String email = adminLoginRequest.email();
        User admin = userRepository.findBySocialEmail(email).orElseThrow(() -> new UserException(StatusCode.NOT_FOUND_USER));

        if (!admin.getRole().equals(Role.ROLE_ADMIN)) throw new UserException(StatusCode.ROLE_ACCESS_ERROR);
        JwtDto jwtDto = adminJwt(adminLoginRequest.changeLoginRequest());
        return new AuthMessage(jwtDto,StatusCode.LOGIN.getMessage());
    }
}
