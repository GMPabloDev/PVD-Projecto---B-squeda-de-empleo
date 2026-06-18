package io.gianmarco.pvd.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.gianmarco.pvd.domain.exceptions.DomainException;
import io.gianmarco.pvd.domain.exceptions.ErrorType;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(
                        MethodArgumentNotValidException ex) {
                List<String> errors = ex
                                .getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                                .toList();

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                400,
                                "Validation Error",
                                "Invalid request body",
                                errors);

                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleInvalidJson(
                        HttpMessageNotReadableException ex) {
                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                400,
                                "Malformed JSON",
                                "Request body is invalid or missing fields",
                                List.of(ex.getMostSpecificCause().getMessage()));

                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
                var response = new ErrorResponse(
                                LocalDateTime.now(),
                                500,
                                "INTERNAL_SERVER_ERROR",
                                "An unexpected error occurred.",
                                List.of());
                return ResponseEntity.status(500).body(response);
        }

        record ErrorResponse(
                        LocalDateTime timestamp,
                        int status,
                        String error,
                        String message,
                        List<String> details) {
        }

        @ExceptionHandler(DomainException.class)
        public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
                HttpStatus status = mapErrorTypeToHttpStatus(ex.getType());

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getPublicMessage(),
                                List.of(ex.getMessage()));

                return ResponseEntity.status(status).body(response);
        }

        private HttpStatus mapErrorTypeToHttpStatus(ErrorType errorType) {
                return switch (errorType) {
                        case CONFLICT -> HttpStatus.CONFLICT; // 409
                        case NOT_FOUND -> HttpStatus.NOT_FOUND; // 404
                        case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED; // 401
                        case FORBIDDEN -> HttpStatus.FORBIDDEN; // 403
                        case VALIDATION -> HttpStatus.BAD_REQUEST; // 400
                        default -> HttpStatus.INTERNAL_SERVER_ERROR; // 500
                };
        }
}
