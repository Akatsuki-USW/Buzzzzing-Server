package bokjak.bokjakserver.domain.location.exception;

import bokjak.bokjakserver.common.exception.BuzException;
import bokjak.bokjakserver.common.exception.StatusCode;

public class LocationException extends BuzException {
    public LocationException(StatusCode statusCode) {
        super(statusCode);
    }

    public LocationException(StatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
