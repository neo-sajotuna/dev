package com.sparta.newneoboardbuddy.config;

import com.sparta.newneoboardbuddy.common.exception.InvalidRequestException;
import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(CommonException.class)
    public ResponseEntity<Map<String, Object>> CommonException(CommonException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getErrorResponse(status, e.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, Object>> invalidRequestExceptionException(InvalidRequestException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, Object>> handleAuthException(AuthException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Map<String, Object>> handleServerException(ServerException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return getErrorResponse(status, ex.getMessage());
    }

    public ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", status.value());
        errorResponse.put("message", message);
        errorResponse.put("data", null);

        return new ResponseEntity<>(errorResponse, status);
    }
}

