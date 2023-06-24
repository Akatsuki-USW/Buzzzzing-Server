package bokjak.bokjakserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    /**
     * User
     */
    NOT_FOUND_USER(404, 2000, "not found user error."),
    NICKNAME_DUPLICATION(409, 2010, "duplicated nickname error."),
    BANNED_USER(400,2020,"banned user error"),
    BLACKLIST_BANNED_USER(403, 2030, "blacklist user error."),
    SOCIAL_TYPE_ERROR(400,2040,"invalid social type error."),
    REVOKE_USER(403,2050,"revoke user error"),
    NICKNAME_VALIDATE_ERROR(400,2060,"invalid nickname error"),
    BLOCK_ERROR(400,2070,"not found blockId error"),
    IS_BLOCKED_ERROR(400,2080,"isblocked error"),
    /**
     * Auth
     */
    // success
    LOGIN(200, 1001, "account exist, process login."),
    SIGNUP_COMPLETE(200, 1011, "signup complete, access token is issued."),

    // fail

    FILTER_ACCESS_DENIED(401, 1000, "access denied."),
    FILTER_ROLE_FORBIDDEN(403, 1010, "role forbidden."),
    SIGNUP_TOKEN_ERROR(400, 1020, "invalid sign up token error."),
    NOT_FOUND_REFRESH_TOKEN(404,1030,"not found refresh token"),
    NEED_TO_SIGNUP(404, 1040, "need to signup, X-ACCESS-TOKEN is issued."),
    ENCRYPTION_FAILURE(400,1050,"encryption failure"),
    DECRYPTION_FAILURE(400,1060,"decryption failure"),
    REVOKE_ERROR(404,1070,"revoke error"),
    IS_NOT_REFRESH(400, 1070, "this token is not refresh token."),
    EXPIRED_REFRESH(400,1080,"expired refresh token");

    private final int HttpCode;
    private final int statusCode;
    private final String message;
}
