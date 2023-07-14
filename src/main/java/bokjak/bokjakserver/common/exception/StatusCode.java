package bokjak.bokjakserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    /**
     * Common
     */
    INTERNAL_SERVER_ERROR(500,  -1, "internal server error."),
    COMMON_BAD_REQUEST(400, 9000, ""),
    INVALID_INPUT_VALUE(400, 9010, "invalid input value."),
    METHOD_NOT_ALLOWED(405, 9020, "method not allowed."),
    HTTP_CLIENT_ERROR(400, 9030, "http client error."),
    INVALID_REQUEST_PARAM(400, 9100, "invalid request param."),

    AWS_S3_UPLOAD_FAIL(400, 9040, "AWS S3 upload fail."),
    AWS_S3_DELETE_FAIL(400, 9050, "AWS S3 delete fail."),
    AWS_S3_FILE_SIZE_EXCEEDED(400, 9060, "exceeded file size."),
    AWS_S3_FILE_TYPE_INVALID(400, 9070, "invalid file type."),
    /**
     * User
     */
    NOT_FOUND_USER(404, 2000, "not found user error."),
    NICKNAME_DUPLICATION(409, 2010, "duplicated nickname error."),
    BANNED_USER(403,2020,"banned user error"),
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
    EXPIRED_REFRESH(400,1080,"expired refresh token"),

    /**
     * Location & Congestion
     */
    CHOICE_NOT_EXIST(404, 5010, "not found choice."),
    NOT_FOUND_LOCATION(404, 5100, "not found location."),
    NOT_FOUND_CONGESTION(404, 5110, "not found congestion."),
    NOT_FOUND_DAILY_CONGESTION_STAT(404, 5120, "not found daily congestion statistics."),
    NOT_FOUND_WEEKLY_CONGESTION_STAT(404, 5130, "not found weekly congestion statistics."),

    /**
     * report
     */

    NOT_FOUND_REPORTED_USER(404, 3000,"not found reported user error."),
    REPORT_DUPLICATION(400, 3010, "duplicate report."),
    NOT_FOUND_REPORT_TARGET(404,3020,"report target not found."),
    ALREADY_BAN_USER(400,3030,"already ban user."),
    OVER_CONTENT_LENGTH(400,3080,"limit of the number of words.");

    private final int HttpCode;
    private final int statusCode;
    private final String message;
}
