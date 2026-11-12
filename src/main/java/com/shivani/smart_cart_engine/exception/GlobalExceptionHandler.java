package com.shivani.smart_cart_engine.exception;

import com.shivani.smart_cart_engine.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.shivani.smart_cart_engine.user.exception.DuplicateEmailException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(
            UserNotFoundException ex) {

        Map<String, Object> error = Map.of(
                "message", ex.getMessage(),
                "status", 404
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmail(
            DuplicateEmailException ex) {

        Map<String, Object> error = Map.of(
                "message", ex.getMessage(),
                "status", 409
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}