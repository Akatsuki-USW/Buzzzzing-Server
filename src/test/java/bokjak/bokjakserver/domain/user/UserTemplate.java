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
    private static final String PASSWORD1 = "test1@naver.com";
    private static final String PASSWORD2 = "test2@naver.com";
    private static final String NICKNAME1 = "test";
    private static final String NICKNAME2 = "test2";
    private static final String SOCIALEMAIL1 = "test1@KAKAO";
    private static final String SOCIALEMAIL2 = "test2@KAKAO";
    private static final String PROFILEIMAGEURL = "2031239.com";
    private static final UserStatus USERSTATUS = UserStatus.NORMAL;
    private static final SocialType SOCIALTYPE = SocialType.KAKAO;
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

    public static User makeUser1() {
        return makeTestUser(EMAIL,PASSWORD1,NICKNAME1,SOCIALEMAIL1,PROFILEIMAGEURL,USERSTATUS,SOCIALTYPE,ROLE);
    }
    public static User makeUser2() {
        return makeTestUser(EMAIL,PASSWORD2,NICKNAME2,SOCIALEMAIL2,PROFILEIMAGEURL,USERSTATUS,SOCIALTYPE,ROLE);
    }

}
