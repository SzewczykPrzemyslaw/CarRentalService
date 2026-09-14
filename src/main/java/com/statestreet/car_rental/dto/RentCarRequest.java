package com.statestreet.car_rental.dto;

import com.statestreet.car_rental.validation.StartDate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

public record RentCarRequest(@NotNull CarType type,
                             @NotNull @StartDate Instant startDateTime,
                             @NotNull @Positive Integer numberOfDays) {
}