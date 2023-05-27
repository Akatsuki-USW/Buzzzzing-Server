package bokjak.bokjakserver.domain.user.exeption;

import bokjak.bokjakserver.common.exception.BuzException;
import bokjak.bokjakserver.common.exception.StatusCode;

public class UserException extends BuzException {
    public UserException(StatusCode statusCode) {
        super(statusCode);
    }

    public UserException(StatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
