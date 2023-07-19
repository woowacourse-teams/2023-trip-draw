package dev.tripdraw.exception.common;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "서버가 응답할 수 없습니다.";
    private static final String METHOD_ARGUMENT_NOT_VALID_MESSAGE = "잘못된 요청입니다.";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(e.getMessage(), e);
        return ResponseEntity.status(httpStatus)
                .body(ExceptionResponse.of(httpStatus.name(), INTERNAL_SERVER_ERROR_MESSAGE));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(
                        HttpStatus.BAD_REQUEST.name(),
                        METHOD_ARGUMENT_NOT_VALID_MESSAGE,
                        generateValidationExceptions(e)
                ));
    }

    private List<ValidationException> generateValidationExceptions(BindException e) {
        return e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ValidationException::from)
                .toList();
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleBaseException(BaseException e) {
        ExceptionType exceptionType = e.getExceptionType();
        log.warn(e.getMessage(), e);
        return ResponseEntity.status(exceptionType.getHttpStatus())
                .body(ExceptionResponse.of(exceptionType.name(), exceptionType.getMessage()));
    }
}
