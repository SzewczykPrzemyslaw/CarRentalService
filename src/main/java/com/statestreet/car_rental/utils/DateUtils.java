package com.statestreet.car_rental.utils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private DateUtils() {}

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.systemDefault());
}
