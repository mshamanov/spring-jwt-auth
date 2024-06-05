package dev.mash.jwtauth.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Global controller advice to handle various exceptions when interacting with the rest controller.
 *
 * @author Mikhail Shamanov
 */
@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException e,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        List<String> errors = new ArrayList<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }

        ProblemDetail problemDetail = e.getBody();
        problemDetail.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setDetail(String.join(System.lineSeparator(), errors));

        return ResponseEntity.of(problemDetail).build();
    }

    /**
     * Handles {@link ResponseStatusException}
     *
     * @param e exception
     * @return response
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleException(ResponseStatusException e) {
        return ResponseEntity.of(e.getBody()).build();
    }

    /**
     * Handles any exception
     *
     * @param e       exception
     * @param request current request
     * @return response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(Exception e, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.of(problemDetail).build();
    }
}
