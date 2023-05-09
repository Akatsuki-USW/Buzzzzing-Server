package bokjak.bokjakserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    /**
     * Auth
     */
    SIGNUP_TOKEN_ERROR(400, 1030, "invalid sign up token error.");

    private final int HttpCode;
    private final int statusCode;
    private final String message;
}
