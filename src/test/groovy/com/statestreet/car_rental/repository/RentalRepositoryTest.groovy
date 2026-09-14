package com.statestreet.car_rental.repository

import com.statestreet.car_rental.dto.CarType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import java.time.Instant;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RentalRepositoryTest extends Specification {

    @Autowired
    RentalRepository repository

    @Autowired
    JdbcTemplate jdbcTemplate

    def "should find available cars of requested type"() {
        given:
        def carType = CarType.SEDAN
        def startDateTime = Instant.parse("2026-09-20T10:00:00Z")
        def endDateTime = Instant.parse("2026-09-23T10:00:00Z")

        when:
        def result = repository.findAvailableCars(carType, startDateTime, endDateTime)

        then:
        result.size() == 2
        result.containsAll([1L, 2L])
    }

    def "should create reservation"() {
        given:
        def carId = 2L
        def startDateTime = Instant.parse("2026-09-20T10:00:00Z")
        def endDateTime = Instant.parse("2026-09-23T10:00:00Z")

        when:
        repository.createReservation(carId, startDateTime, endDateTime)

        then:
        def reservations = jdbcTemplate.queryForList(
                """
                SELECT car_id, start_date_time, end_date_time
                FROM reservation
                WHERE car_id = ?
                """,
                carId
        )

        reservations.size() == 1
        reservations[0].car_id == carId
    }

    def "should lock existing car for update"() {
        given:
        def carId = 2L

        when:
        repository.lockCarForUpdate(carId)

        then:
        noExceptionThrown()
    }

    def "should return true when car has no overlapping reservation"() {
        given:
        def carId = 2L
        def startDateTime = Instant.parse("2026-09-20T10:00:00Z")
        def endDateTime = Instant.parse("2026-09-23T10:00:00Z")

        when:
        def result = repository.isCarAvailable(carId, startDateTime, endDateTime)

        then:
        result
    }

    def "should return false when car has overlapping reservation"() {
        given:
        def carId = 1L
        def startDateTime = Instant.parse("2026-09-18T10:00:00Z")
        def endDateTime = Instant.parse("2026-09-19T10:00:00Z")

        when:
        def result = repository.isCarAvailable(carId, startDateTime, endDateTime)

        then:
        !result
    }

    def "should allow reservation immediately after existing reservation"() {
        given:
        def carId = 1L
        def startDateTime = Instant.parse("2026-09-20T10:00:00Z")
        def endDateTime = Instant.parse("2026-09-23T10:00:00Z")

        when:
        def result = repository.isCarAvailable(carId, startDateTime, endDateTime)

        then:
        result
    }

    def "should exclude car with overlapping reservation"() {
        given:
        def carType = CarType.SEDAN
        def startDateTime = Instant.parse("2026-09-18T10:00:00Z")
        def endDateTime = Instant.parse("2026-09-19T10:00:00Z")

        when:
        def result = repository.findAvailableCars(carType, startDateTime, endDateTime)

        then:
        result == [2L, 3L]
    }
}