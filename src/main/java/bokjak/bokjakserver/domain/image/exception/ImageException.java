package bokjak.bokjakserver.domain.image.exception;

import bokjak.bokjakserver.common.exception.StatusCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ImageException extends RuntimeException{
    StatusCode awsS3ErrorCode;

    public ImageException(StatusCode awsS3ErrorCode) {
        super(awsS3ErrorCode.getMessage());
        this.awsS3ErrorCode = awsS3ErrorCode;
    }
}
