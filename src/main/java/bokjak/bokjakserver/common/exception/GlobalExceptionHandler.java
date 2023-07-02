package bokjak.bokjakserver.common.exception;

import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.domain.user.exeption.AuthException;
import bokjak.bokjakserver.util.s3.exception.AwsS3Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static bokjak.bokjakserver.common.dto.ApiResponse.error;
import static bokjak.bokjakserver.common.exception.StatusCode.AWS_S3_FILE_SIZE_EXCEEDED;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BuzException.class)
    public ResponseEntity<?> handle(BuzException ex) {
        log.warn("{}({}) - {}", ex.getClass().getSimpleName(), ex.statusCode.getStatusCode(), ex.getMessage());
        return ResponseEntity
                .status(ex.statusCode.getHttpCode())
                .body(ApiResponse.error(ex.statusCode.getStatusCode(),ex.statusCode.getMessage()));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handle(AuthException ex) {
        log.warn("{}({}) - {}", ex.getClass().getSimpleName(), ex.statusCode.getStatusCode(), ex.getMessage());
        return ResponseEntity
                .status(ex.statusCode.getHttpCode())
                .body(ApiResponse.error(ex.statusCode.getStatusCode(),ex.data, ex.statusCode.getMessage()));

    }

    // 기타 런타임 예외 (예외처리가 제대로 되지 않았거나 코드 자체의 문제인 경우일 확률 높음)
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleInternalError(final RuntimeException ex) {
        log.error("Uncaught {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(StatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), ex.getMessage()));
    }

    // Aws S3
    @ExceptionHandler(AwsS3Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponse<?> handleAwsS3Error(final AwsS3Exception ex) {
        log.warn("{} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        return error(ex.getAwsS3ErrorCode().getStatusCode(), ex.getMessage());
    }

    // 파일 업로드 용량 초과 (servlet.multipart.max-file-size)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponse<?> handleMaxUploadSizeException(final MaxUploadSizeExceededException ex) {
        log.warn("{} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        return error(AWS_S3_FILE_SIZE_EXCEEDED.getStatusCode(), AWS_S3_FILE_SIZE_EXCEEDED.getMessage());
    }
}


