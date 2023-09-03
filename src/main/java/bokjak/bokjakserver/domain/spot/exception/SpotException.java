package bokjak.bokjakserver.domain.spot.exception;

import bokjak.bokjakserver.common.exception.BuzException;
import bokjak.bokjakserver.common.exception.StatusCode;

public class SpotException extends BuzException {

    public SpotException(StatusCode statusCode) {
        super(statusCode);
    }

    public SpotException(StatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
