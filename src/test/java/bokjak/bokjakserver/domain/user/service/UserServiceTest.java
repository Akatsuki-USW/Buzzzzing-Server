package bokjak.bokjakserver.domain.user.service;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.config.redis.RedisService;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.user.UserTemplate;
import bokjak.bokjakserver.domain.user.dto.AuthDto.AuthMessage;
import bokjak.bokjakserver.domain.user.dto.UserDto.NicknameResponse;
import bokjak.bokjakserver.domain.user.dto.UserDto.UserInfoResponse;
import bokjak.bokjakserver.domain.user.exeption.UserException;
import bokjak.bokjakserver.domain.user.model.RevokeUser;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.repository.RevokeUserRepository;
import bokjak.bokjakserver.domain.user.repository.UserBlockUserRepository;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import bokjak.bokjakserver.util.CustomEncryptUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    KakaoService kakaoService;
    @Mock
    CustomEncryptUtil customEncryptUtil;
    @Mock
    RevokeUserRepository revokeUserRepository;
    @Mock
    UserBlockUserRepository userBlockUserRepository;
    @Mock
    RedisService redisService;


    static User user = UserTemplate.makeDummyUserA();

    @BeforeEach
    void setUp() {
        UserDetails userDetails = new PrincipalDetails(user);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user.getSocialEmail(),user.getPassword()));
    }

    @Test
    @DisplayName("유저정보조회 - 성공")
    public void getUserInfo() throws Exception {
        //given
        User testUser1 = UserTemplate.makeDummyUserA();
        given(userRepository.findById(any())).willReturn(Optional.ofNullable(testUser1));
        //when
        UserInfoResponse userInfo = userService.getUserInfo(testUser1.getId());
        //then
        assertThat(userInfo.email()).isEqualTo(testUser1.getEmail());
        assertThat(userInfo.nickname()).isEqualTo(testUser1.getNickname());
        assertThat(userInfo.profileImageUrl()).isEqualTo(testUser1.getProfileImageUrl());
    }

    @Test
    @DisplayName("유저정보조회 - 실패")
    public void getUserInfo_fail() throws Exception {
        //given
        given(userRepository.findById(any())).willReturn(Optional.empty());
        //then
        assertThatThrownBy(()-> userService.getUserInfo(any()))
                .isInstanceOf(UserException.class)
                .hasMessage(StatusCode.NOT_FOUND_USER.getMessage());
    }

    @Test
    @DisplayName("닉네임 검증 - 실패(패턴)")
    public void nicknameValidate_fail() throws Exception {
        //given

        //when - then
        assertThatThrownBy(()-> userService.isDuplicateNickname("#@오류나야해요"))
                .isInstanceOf(UserException.class)
                .hasMessage(StatusCode.NICKNAME_VALIDATE_ERROR.getMessage());
    }

    @Test
    @DisplayName("닉네임 검증 - 실패(중복)")
    public void nicknameDuplicate_fail() throws Exception {
        //given
        given(userRepository.existsByNickname(any())).willReturn(true);
        //when

        //when - then
        assertThatThrownBy(()-> userService.isDuplicateNickname("중복닉네임"))
                .isInstanceOf(UserException.class)
                .hasMessage(StatusCode.NICKNAME_DUPLICATION.getMessage());
    }

    @Test
    @DisplayName("닉네임 검증 - 성공")
    public void nickname_success() throws Exception {
        //given
        given(userRepository.existsByNickname(any())).willReturn(false);
        //when
        NicknameResponse response = userService.isDuplicateNickname("통과");
        //then
        assertThat(response.isAvailableNickname()).isTrue();
    }

//    @Test
//    @DisplayName("회원 차단 - 실패")
//    public void block_fail() throws Exception {
//        //given
//        User testUser1 = UserTemplate.makeDummyUserA();
//        User testUser2 = UserTemplate.makeDummyUserB();
//        given(userRepository.findBySocialEmail(any())).willReturn(Optional.ofNullable(testUser1));
//        given(userRepository.findById(any())).willReturn(Optional.ofNullable(testUser2));
//        given(userBlockUserRepository.existsByBlockerUserAndBlockedUser(any(),any())).willReturn(true);
//        //
//
//        //when-then
//        assertThatThrownBy(()-> userService.hideUser(new UserDto.HideRequest(1L)))
//                .isInstanceOf(UserException.class)
//                .hasMessage(StatusCode.IS_BLOCKED_ERROR.getMessage());
//    }

//    @Test
//    @DisplayName("회원 차단 - 성공")
//    public void block_success() throws Exception {
//        //given
//        User testUser1 = UserTemplate.makeDummyUserA();
//        User testUser2 = UserTemplate.makeDummyUserB();
//        given(userRepository.findBySocialEmail(any())).willReturn(Optional.ofNullable(testUser1));
//        given(userRepository.findById(any())).willReturn(Optional.ofNullable(testUser2));
//        given(userBlockUserRepository.existsByBlockerUserAndBlockedUser(any(),any())).willReturn(false);
//        //when
//        UserDto.HideResponse hideResponse = userService.hideUser(new UserDto.HideRequest(1L));
//
//        //then
//        assertThat(hideResponse.blockedResult()).isTrue();
//    }

    @Test
    @DisplayName("회원 탈퇴 - 성공")
    public void revokeUser() throws Exception {
        //given
        User testUser1 = UserTemplate.makeDummyUserA();
        given(userRepository.findById(any())).willReturn(Optional.ofNullable(testUser1));
        String[] split = testUser1.getSocialEmail().split("@");
        given(kakaoService.revokeKakao(split[0])).willReturn(true);
        given(customEncryptUtil.hash(any())).willReturn("1235124213213SAASD");
        RevokeUser revokeUser = RevokeUser.builder().socialEmail("1235124213213SAASD").revokedAt(LocalDateTime.now()).build();
        //when
        AuthMessage revoke = userService.revoke(testUser1.getId());
        //then
        assertThat(revokeUser.getSocialEmail()).isEqualTo("1235124213213SAASD");
        assertThat(revoke.detailData()).isEqualTo(true);
    }

    @Test
    @DisplayName("회원 탈퇴 - 실패")
    public void revokeError() throws Exception {
        //given
        User testUser = UserTemplate.makeDummyUserA();
        given(userRepository.findById(any())).willReturn(Optional.ofNullable(testUser));
        String[] split = testUser.getSocialEmail().split("@");
        given(kakaoService.revokeKakao(split[0])).willReturn(false);
        //when
        AuthMessage revoke = userService.revoke(testUser.getId());
        //then
        assertThat(revoke.detailData()).isEqualTo(false);

    }
}