package com.seed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {
    private final ObjectMapper objectMapper;

    public ValidationExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatErrorMessage)
                .toList();

        return ResponseEntity.badRequest().body(Map.of("errors", errors));
    }

    private String formatErrorMessage(FieldError fieldError) {
        PropertyNamingStrategy mapping = objectMapper.getPropertyNamingStrategy();
        String mappedFieldName = mapping.nameForField(null, null, fieldError.getField());

        return String.format("%s.%s: %s", fieldError.getObjectName(), mappedFieldName, fieldError.getDefaultMessage());
    }
}
