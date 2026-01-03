package com.kood.movieapi.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> error = new HashMap<>();
        String name = ex.getName();
        String value = ex.getValue() != null ? ex.getValue().toString() : "null";
        error.put("error", "Invalid parameter '" + name + "' with value '" + value + "'");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();

        Throwable root = getRootCause(ex);
        if (root instanceof InvalidFormatException ife) {
            String field = extractFieldFromPath(ife);
            String targetType = ife.getTargetType() != null ? ife.getTargetType().getSimpleName() : "value";
            Object value = ife.getValue();
            if (targetType.equals("LocalDate") || root.getCause() instanceof DateTimeParseException) {
                error.put("error", String.format("Invalid date format for field '%s': '%s'. Expected format is YYYY-MM-DD.",
                        field, String.valueOf(value)));
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            error.put("error", String.format("Invalid %s for field '%s': '%s'", targetType, field, String.valueOf(value)));
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        if (root instanceof DateTimeParseException dtpe) {
            error.put("error", String.format("Invalid date format: %s. Expected format is YYYY-MM-DD.", dtpe.getParsedString()));
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        error.put("error", "Malformed JSON request or invalid field values");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private String extractFieldFromPath(JsonMappingException jme) {
        return jme.getPath().stream()
                .map(ref -> Optional.ofNullable(ref.getFieldName()).orElse(String.valueOf(ref.getIndex())))
                .reduce((a, b) -> a + "." + b)
                .orElse("unknown");
    }

    private Throwable getRootCause(Throwable ex) {
        Throwable result = ex;
        while (result.getCause() != null && result.getCause() != result) {
            result = result.getCause();
        }
        return result;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
