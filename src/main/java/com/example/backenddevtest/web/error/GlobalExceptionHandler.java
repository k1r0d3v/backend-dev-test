package com.example.backenddevtest.web.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

/**
 * The application global exception handler.
 * <p>
 * To handle {@code NoHandlerFoundException} exceptions, the DispatcherServlet option of throwExceptionIfNoHandlerFound must be false.
 * For more information see {@link org.springframework.web.servlet.DispatcherServlet#setThrowExceptionIfNoHandlerFound(boolean)}.
 * <p>
 * Another method to control the default spring error response is to override {@link org.springframework.boot.web.servlet.error.DefaultErrorAttributes}.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private record ValidationMessage(String message) {};

    /**
     * Handler for object validations.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request, HttpServletRequest httpServletRequest) {
        List<ValidationMessage> validations = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .map(ValidationMessage::new)
                .toList();

        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(Date.from(Instant.now()));
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError("Your request parameters didn't validate.");
        response.setPath(httpServletRequest.getRequestURI());
        response.addAdditionalAttribute("validation-messages", validations);

        return handleExceptionInternal(exception, response, request);
    }

    /**
     * Default handler for all non handled exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request, HttpServletRequest httpServletRequest) {
        logger.error(exception);

        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(Date.from(Instant.now()));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        response.setPath(httpServletRequest.getRequestURI());

        return handleExceptionInternal(exception, response, request);
    }

    /**
     * Helper method that unify responses of type ErrorResponse.
     */
    private ResponseEntity<Object> handleExceptionInternal(Exception exception, ErrorResponse errorResponse, WebRequest request) {
        HttpStatus status = HttpStatus.resolve(errorResponse.getStatus());

        assert status != null;
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), status, request);
    }
}
