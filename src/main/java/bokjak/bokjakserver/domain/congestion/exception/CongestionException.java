package bokjak.bokjakserver.domain.congestion.exception;

import bokjak.bokjakserver.common.exception.BuzException;
import bokjak.bokjakserver.common.exception.StatusCode;

public class CongestionException extends BuzException {
    public CongestionException(StatusCode statusCode) {
        super(statusCode);
    }

    public CongestionException(StatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
