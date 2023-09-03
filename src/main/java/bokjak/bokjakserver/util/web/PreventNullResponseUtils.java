package bokjak.bokjakserver.util.web;


public class PreventNullResponseUtils {
    private static final String ALTERNATIVE_NICKNAME = "알 수 없음";
    public static final String ALTERNATIVE_PROFILE_IMAGE_URL = "";

    public static String resolveUserNicknameFromNullable(String nickname) {
        return nickname != null ? nickname : ALTERNATIVE_NICKNAME;
    }

    public static String resolveUserProfileImageUrlFromNullable(String profileImageUrl) {
        return profileImageUrl != null ? profileImageUrl : ALTERNATIVE_PROFILE_IMAGE_URL;
    }
}
