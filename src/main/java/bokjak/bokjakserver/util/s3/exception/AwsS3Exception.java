package bokjak.bokjakserver.util.s3.exception;

import bokjak.bokjakserver.common.exception.StatusCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AwsS3Exception extends RuntimeException{
    StatusCode awsS3ErrorCode;

    public AwsS3Exception(StatusCode awsS3ErrorCode) {
        super(awsS3ErrorCode.getMessage());
        this.awsS3ErrorCode = awsS3ErrorCode;
    }
}
