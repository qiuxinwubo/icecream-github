package com.icecream.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleException(Exception e) {
        return Map.of("status", 1, "msg", e.getMessage());
    }
}