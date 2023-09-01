package dev.tripdraw.common.exception;

import static dev.tripdraw.common.log.MdcToken.REQUEST_ID;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String LOG_FORMAT = "[%s] %s";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "서버가 응답할 수 없습니다.";
    private static final String METHOD_ARGUMENT_NOT_VALID_MESSAGE = "잘못된 요청입니다.";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        HttpStatus httpStatus = INTERNAL_SERVER_ERROR;
        log.error(String.format(LOG_FORMAT, MDC.get(REQUEST_ID.key()), e.getMessage()), e);
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

        return ResponseEntity.status(BAD_REQUEST)
                .body(new ExceptionResponse(
                        BAD_REQUEST.name(),
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
        ExceptionType exceptionType = e.exceptionType();
        log.info(String.format(LOG_FORMAT, MDC.get(REQUEST_ID.key()), e.getMessage()), e);
        return ResponseEntity.status(exceptionType.httpStatus())
                .body(ExceptionResponse.of(exceptionType.name(), exceptionType.message()));
    }
}
