package com.statestreet.car_rental.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartDateValidator.class)
@Documented
public @interface StartDate {

    String message() default "Reservation date cannot be in the past.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}