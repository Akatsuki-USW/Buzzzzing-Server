package bokjak.bokjakserver.domain.ban.exception;

import bokjak.bokjakserver.common.exception.BuzException;
import bokjak.bokjakserver.common.exception.StatusCode;

public class BanException extends BuzException {
    public BanException(StatusCode statusCode) {
        super(statusCode);
    }

    public BanException(StatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
