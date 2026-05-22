package com.gordeok.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 유효성 검사 실패 (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                        (a, b) -> a));
        return error(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.", errors);
    }

    // 비즈니스 예외 (400 / 409 구분)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegal(IllegalArgumentException ex) {
        String msg = ex.getMessage();
        HttpStatus status = (msg != null && msg.contains("이미 사용 중인") || msg != null && msg.contains("이미 후기"))
                ? HttpStatus.CONFLICT
                : HttpStatus.BAD_REQUEST;
        return error(status, msg, null);
    }

    // 404
    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(jakarta.persistence.EntityNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    // 기타 서버 오류 (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.", null);
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message, Object detail) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("message", message);
        if (detail != null) body.put("detail", detail);
        return ResponseEntity.status(status).body(body);
    }
}
