package bokjak.bokjakserver.domain.user.exeption;

import bokjak.bokjakserver.common.exception.BuzException;
import bokjak.bokjakserver.common.exception.StatusCode;

import java.util.HashMap;

public class AuthException extends BuzException {
    public Object data;

    public AuthException(StatusCode statusCode) {
        super(statusCode);
        this.data = new HashMap<String,String>();
    }
    public AuthException(StatusCode statusCode, String message) {
        super(statusCode, message);
        this.data= new HashMap<String, String>();
    }
    public AuthException(StatusCode statusCode, Object data) {
        super(statusCode);
        this.data = data;
    }
}
