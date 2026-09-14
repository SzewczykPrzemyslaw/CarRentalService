package com.statestreet.car_rental.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;

public class StartDateValidator implements ConstraintValidator<StartDate, Instant> {

    @Override
    public boolean isValid(Instant startDate, ConstraintValidatorContext context) {
        return startDate == null || !startDate.isBefore(Instant.now());
    }
}
