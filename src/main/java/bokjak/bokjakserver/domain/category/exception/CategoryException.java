package bokjak.bokjakserver.domain.category.exception;

import bokjak.bokjakserver.common.exception.BuzException;
import bokjak.bokjakserver.common.exception.StatusCode;

public class CategoryException extends BuzException {
    public CategoryException(StatusCode statusCode) {
        super(statusCode);
    }

    public CategoryException(StatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
