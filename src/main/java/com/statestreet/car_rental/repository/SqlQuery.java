package com.statestreet.car_rental.repository;

public final class SqlQuery {

    private SqlQuery() {
    }

    public static final String INSERT_RESERVATION = """
            INSERT INTO reservation (car_id, start_date_time, end_date_time)
            VALUES (:carId, :startDateTime, :endDateTime)
            """;

    public static final String FIND_AVAILABLE_CARS = """
            SELECT c.id
            FROM car c
            WHERE c.type = :carType
              AND NOT EXISTS (
                  SELECT 1
                  FROM reservation r
                  WHERE r.car_id = c.id
                    AND r.start_date_time < :endDateTime
                    AND r.end_date_time > :startDateTime
              )
            """;

    public static final String LOCK_CAR = """
            SELECT id
            FROM car
            WHERE id = :carId
            FOR UPDATE
            """;

    public static final String IS_CAR_AVAILABLE = """
            SELECT NOT EXISTS (
                SELECT 1
                FROM reservation r
                WHERE r.car_id = :carId
                  AND r.start_date_time < :endDateTime
                  AND r.end_date_time > :startDateTime
            )
            """;

    static class Fields {
        public static final String CAR_TYPE = "carType";
        public static final String START_DATE_TIME = "startDateTime";
        public static final String END_DATE_TIME = "endDateTime";
        public static final String CAR_ID = "carId";
    }
}
