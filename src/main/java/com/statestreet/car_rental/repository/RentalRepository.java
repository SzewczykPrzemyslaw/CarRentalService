package com.statestreet.car_rental.repository;

import com.statestreet.car_rental.dto.CarType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static com.statestreet.car_rental.repository.SqlQuery.Fields.*;

@Repository
public class RentalRepository {

    private final NamedParameterJdbcTemplate queryTemplate;

    public RentalRepository(NamedParameterJdbcTemplate queryTemplate) {
        this.queryTemplate = queryTemplate;
    }

    public List<Long> findAvailableCars(CarType carType, Instant startDateTime, Instant endDateTime) {
        Map<String, Object> parameters = Map.of(
                CAR_TYPE, carType.name(),
                START_DATE_TIME, startDateTime.atOffset(ZoneOffset.UTC),
                END_DATE_TIME, endDateTime.atOffset(ZoneOffset.UTC)
        );

        return queryTemplate.queryForList(SqlQuery.FIND_AVAILABLE_CARS, parameters, Long.class);
    }

    public void createReservation(Long carId, Instant startDateTime, Instant endDateTime) {
        Map<String, Object> parameters = Map.of(
                CAR_ID, carId,
                START_DATE_TIME, startDateTime.atOffset(ZoneOffset.UTC),
                END_DATE_TIME, endDateTime.atOffset(ZoneOffset.UTC)
        );

        queryTemplate.update(SqlQuery.INSERT_RESERVATION, parameters);
    }

    public void lockCarForUpdate(Long carId) {
        queryTemplate.queryForObject(SqlQuery.LOCK_CAR, Map.of(CAR_ID, carId), Long.class);
    }

    public boolean isCarAvailable(Long carId, Instant startDateTime, Instant endDateTime) {
        Map<String, Object> parameters = Map.of(
                CAR_ID, carId,
                START_DATE_TIME, startDateTime.atOffset(ZoneOffset.UTC),
                END_DATE_TIME, endDateTime.atOffset(ZoneOffset.UTC)
        );

        return Boolean.TRUE.equals(queryTemplate.queryForObject(SqlQuery.IS_CAR_AVAILABLE, parameters, Boolean.class));
    }
}
