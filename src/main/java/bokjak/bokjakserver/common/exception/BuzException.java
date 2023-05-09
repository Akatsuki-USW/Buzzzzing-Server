package bokjak.bokjakserver.common.exception;

public class BuzException extends RuntimeException {
    public StatusCode statusCode;
    public BuzException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode=statusCode;
    }

    public BuzException(StatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
