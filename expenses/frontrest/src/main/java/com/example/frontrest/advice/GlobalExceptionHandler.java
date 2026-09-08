package com.example.frontrest.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            Exception ex,
            HttpServletRequest request) {
 
        log.error("[API] Erreur interne non gérée — path={} exception={} message={}",
                  request.getRequestURI(),
                  ex.getClass().getSimpleName(),
                  ex.getMessage(), ex);
        Map<String, Object> map = new HashMap<>();
        
        map.put("status",HttpStatus.INTERNAL_SERVER_ERROR);
        map.put("message", "Une erreur interne est survenue. " +
                "Notre équipe a été notifiée. Réessayez ultérieurement.");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(map);
    }
}