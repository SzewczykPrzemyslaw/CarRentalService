package com.statestreet.car_rental.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ExceptionResponse(int status, String error, String message, String path, Instant timestamp) {

    public static ExceptionResponse of(HttpStatus status, String message, String path) {
        return new ExceptionResponse(status.value(), status.getReasonPhrase(), message, path, Instant.now());
    }
}
