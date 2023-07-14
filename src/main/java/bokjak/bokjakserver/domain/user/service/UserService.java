package bokjak.bokjakserver.domain.user.service;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.config.jwt.RefreshTokenRepository;
import bokjak.bokjakserver.domain.user.dto.AuthDto.AuthMessage;
import bokjak.bokjakserver.domain.user.exeption.UserException;
import bokjak.bokjakserver.domain.user.model.*;
import bokjak.bokjakserver.domain.user.repository.RevokeUserRepository;
import bokjak.bokjakserver.domain.user.repository.UserBlockUserRepository;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import bokjak.bokjakserver.util.CustomEncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static bokjak.bokjakserver.config.security.SecurityUtils.getCurrentUserSocialEmail;
import static bokjak.bokjakserver.domain.user.dto.UserDto.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final KakaoService kakaoService;
    private final RevokeUserRepository revokeUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomEncryptUtil customEncryptUtil;
    private final UserBlockUserRepository userBlockUserRepository;

    public User getCurrentUser() {
        return userRepository.findBySocialEmail(getCurrentUserSocialEmail()).orElseThrow(() -> new UserException(StatusCode.NOT_FOUND_USER));
    }

    public UserInfoResponse getUserInfo() {
        User user = getCurrentUser();
        return new UserInfoResponse(user.getEmail(),user.getNickname(),user.getProfileImageUrl());
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(StatusCode.NOT_FOUND_USER));
    }

    public NicknameResponse isDuplicateNickname(String nickname) {
        validateDuplicateNickname(nickname);
        return new NicknameResponse(true);
    }

    public void validateDuplicateNickname(String nickname) {
//        String pattern = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$";
        String pattern = "^[0-9a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]{2,16}$";
        if (!Pattern.matches(pattern, nickname)) {
            throw new UserException(StatusCode.NICKNAME_VALIDATE_ERROR);
        }
        if (userRepository.existsByNickname(nickname)) {
            throw new UserException(StatusCode.NICKNAME_DUPLICATION);
        }
    }

    @Transactional
    public UserInfoResponse updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest) {
        User currentUser = getCurrentUser();
        if (!currentUser.getNickname().equals(updateUserInfoRequest.nickname())) {
            validateDuplicateNickname(updateUserInfoRequest.nickname());
        }

        currentUser.updateUserInfo(updateUserInfoRequest);

        return UserInfoResponse.of(currentUser);
    }

    @Transactional
    public HideResponse hideUser(HideRequest hideRequest) {
        User currentUser = getCurrentUser();
        User blockedUser = userRepository.findById(hideRequest.blockUserId()).orElseThrow(() -> new UserException(StatusCode.NOT_FOUND_USER));

        boolean exists = userBlockUserRepository.existsByBlockerUserAndBlockedUser(currentUser, blockedUser);
        if (exists) throw new UserException(StatusCode.IS_BLOCKED_ERROR);

        UserBlockUser userBlockUser = UserBlockUser.builder()
                .blockerUser(currentUser)
                .blockedUser(blockedUser)
                .build();

        userBlockUserRepository.save(userBlockUser);
        currentUser.addBlockedUser(userBlockUser);
        blockedUser.addBlockerUser(userBlockUser);

        return new HideResponse(true);
    }

    @Transactional
    public AuthMessage revoke() {
        User user = getCurrentUser();
        SocialType socialType = user.getSocialType();
        String[] split = user.getSocialEmail().split("@");

        boolean result = switch (socialType) {
            case KAKAO -> kakaoService.revokeKakao(split[0]);
            default -> throw new UserException(StatusCode.SOCIAL_TYPE_ERROR);
        };

        if (result) {
            RevokeUser revokeUser = RevokeUser.builder()
                    .socialEmail(customEncryptUtil.hash(user.getSocialEmail()))
                    .revokedAt(LocalDateTime.now()).build();
            revokeUserRepository.save(revokeUser);
            refreshTokenRepository.deleteByUser(user);
            /**
             * 삭제할 곳 추가 ex)feed, comment 등
             */
            user.deletedUser();
        }
        return new AuthMessage(result, "Revoke result: " + result);
    }

}
