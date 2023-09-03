package bokjak.bokjakserver.domain.comment.exception;

import bokjak.bokjakserver.common.exception.BuzException;
import bokjak.bokjakserver.common.exception.StatusCode;

public class CommentException extends BuzException {
    public CommentException(StatusCode statusCode) {
        super(statusCode);
    }

    public CommentException(StatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
