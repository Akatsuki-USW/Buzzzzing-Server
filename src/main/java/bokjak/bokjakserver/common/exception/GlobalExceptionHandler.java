package bokjak.bokjakserver.common.exception;

import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.domain.user.exeption.AuthException;
import bokjak.bokjakserver.util.s3.exception.AwsS3Exception;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

import static bokjak.bokjakserver.common.dto.ApiResponse.error;
import static bokjak.bokjakserver.common.exception.StatusCode.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BuzException.class)
    public ResponseEntity<?> handle(BuzException ex) {
        log.warn("{}({}) - {}", ex.getClass().getSimpleName(), ex.statusCode.getStatusCode(), ex.getMessage());
        return ResponseEntity
                .status(ex.statusCode.getHttpCode())
                .body(ApiResponse.error(ex.statusCode.getStatusCode(), ex.statusCode.getMessage()));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handle(AuthException ex) {
        log.warn("{}({}) - {}", ex.getClass().getSimpleName(), ex.statusCode.getStatusCode(), ex.getMessage());
        return ResponseEntity
                .status(ex.statusCode.getHttpCode())
                .body(ApiResponse.error(ex.statusCode.getStatusCode(), ex.data, ex.statusCode.getMessage()));

    }

    /**
     * Client Error 4xx
     * : 요청에 문제가 있는 경우
     */
    // AWS S3 버킷 정책에 맞지 않는 요청
    @ExceptionHandler(AwsS3Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponse<?> handleAwsS3Error(final AwsS3Exception ex) {
        log.warn("{} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        return error(ex.getAwsS3ErrorCode().getStatusCode(), ex.getMessage());
    }

    // 파일 업로드 용량 초과
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponse<?> handleMaxUploadSizeException(final MaxUploadSizeExceededException ex) {
        log.warn("{} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        return error(AWS_S3_FILE_SIZE_EXCEEDED.getStatusCode(), AWS_S3_FILE_SIZE_EXCEEDED.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleIOException(IOException ex) {
        log.warn("{} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        return error(INVALID_INPUT_VALUE.getStatusCode(), ex.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    protected ApiResponse<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn("{} - {}", ex.getMessage(), ex.getMessage());
        return error(METHOD_NOT_ALLOWED.getStatusCode(), ex.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleHttpClientErrorException(HttpClientErrorException ex) {
        log.warn("{} - {}", ex.getClass().getName(), ex.getMessage());
        return error(HTTP_CLIENT_ERROR.getStatusCode(), ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponse<?> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("{} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        return error(INVALID_INPUT_VALUE.getStatusCode(), ex.getConstraintViolations().iterator().next().getMessage());
    }

    @ExceptionHandler(BindException.class)// @Valid 실패 Exception
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponse<?> handleValidationException(BindException ex) {
        log.warn("ValidationException({}) - {}", ex.getClass().getSimpleName(), ex.getMessage());
        StringBuilder reason = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            reason.append(fieldError.getDefaultMessage()).append(",");
        }
        return error(INVALID_INPUT_VALUE.getStatusCode(), reason.toString());
    }

    @ExceptionHandler(ServletRequestBindingException.class)// @RequestParam 누락
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleServletRequestBindingException(ServletRequestBindingException ex) {
        log.warn("{} - {}", ex.getClass().getName(), ex.getMessage());
        return error(HTTP_CLIENT_ERROR.getStatusCode(), ex.getMessage());
    }

    /**
     * Internal Server Error 5xx
     * : 예외처리가 제대로 되지 않았거나 코드 자체의 문제인 경우일 확률 높음
     * 무조건 코드를 고치거나 해당 예외처리 핸들러를 추가해줘야 함!
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleInternalError(final Exception ex) {
        log.error("Uncaught {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(StatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), ex.getMessage()));
    }
}


