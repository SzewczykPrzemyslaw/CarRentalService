package com.statestreet.car_rental.exception;

import com.statestreet.car_rental.dto.CarType;
import java.time.Instant;

import static com.statestreet.car_rental.utils.DateUtils.FORMATTER;

public class NoAvailableCarException extends RuntimeException {

    public NoAvailableCarException(CarType type, Instant startDateTime, Instant endDateTime) {
        super("No %s available for the requested period from %s to %s".formatted(
                type, FORMATTER.format(startDateTime), FORMATTER.format(endDateTime)));
    }
}