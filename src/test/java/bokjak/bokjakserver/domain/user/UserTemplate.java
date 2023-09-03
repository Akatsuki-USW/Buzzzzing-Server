package bokjak.bokjakserver.domain.user;

import bokjak.bokjakserver.domain.user.model.Role;
import bokjak.bokjakserver.domain.user.model.SocialType;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.model.UserStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserTemplate {

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private static Long id = 10000L;
    private static final String EMAIL = "test@naver.com";
    private static final String DUMMY_PASSWORD_A = "test1@naver.com";
    private static final String DUMMY_PASSWORD_B = "test2@naver.com";
    private static final String DUMMY_NICKNAME_A = "test";
    private static final String DUMMY_NICKNAME_B = "test2";
    private static final String DUMMY_SOCIAL_EMAIL_A = "test1@KAKAO";
    private static final String DUMMY_SOCIAL_EMAIL_B = "test2@KAKAO";
    private static final String PROFILE_IMAGE_URL = "2031239.com";
    private static final UserStatus USER_STATUS = UserStatus.NORMAL;
    private static final SocialType SOCIAL_TYPE = SocialType.KAKAO;
    private static final Role ROLE = Role.ROLE_USER;


    private static User getMakeUser(String email, String password, String nickname, String socialEmail,
                                    String profileImageUrl, UserStatus userStatus, SocialType socialType, Role role) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .socialEmail(socialEmail)
                .profileImageUrl(profileImageUrl)
                .userStatus(userStatus)
                .socialType(socialType)
                .role(role).build();
    }

    public static User makeTestUser(String email, String password, String nickname, String socialEmail,
                                    String profileImageUrl, UserStatus userStatus, SocialType socialType, Role role) {
        User user = getMakeUser(email, password, nickname, socialEmail, profileImageUrl, userStatus, socialType, role);
        return user;
    }

    public static User makeDummyUserA() {
        return makeTestUser(EMAIL,DUMMY_PASSWORD_A,DUMMY_NICKNAME_A,DUMMY_SOCIAL_EMAIL_A,PROFILE_IMAGE_URL,USER_STATUS,SOCIAL_TYPE,ROLE);
    }
    public static User makeDummyUserB() {
        return makeTestUser(EMAIL,DUMMY_PASSWORD_B,DUMMY_NICKNAME_B,DUMMY_SOCIAL_EMAIL_B,PROFILE_IMAGE_URL,USER_STATUS,SOCIAL_TYPE,ROLE);
    }

}
