package com.statestreet.car_rental.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoAvailableCarException.class)
    public ResponseEntity<ExceptionResponse> handleNoAvailableCar(NoAvailableCarException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity
                .status(status)
                .body(ExceptionResponse.of(status, exception.getMessage(), request.getRequestURI()));
    }
}