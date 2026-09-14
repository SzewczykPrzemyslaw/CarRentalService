package com.statestreet.car_rental.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoAvailableCarException.class)
    public ResponseEntity<ExceptionResponse> handleNoAvailableCar(NoAvailableCarException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        log.info("Reservation request failed: status = '{}', path = '{}', message = '{}'", status.value(), request.getRequestURI(), exception.getMessage());
        return ResponseEntity
                .status(status)
                .body(ExceptionResponse.of(status, exception.getMessage(), request.getRequestURI()));
    }
}