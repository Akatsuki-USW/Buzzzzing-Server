package bokjak.bokjakserver.domain.notification.exception;

import bokjak.bokjakserver.common.exception.BuzException;
import bokjak.bokjakserver.common.exception.StatusCode;

public class NotificationException extends BuzException {

    public NotificationException(StatusCode statusCode) {
        super(statusCode);
    }
}
