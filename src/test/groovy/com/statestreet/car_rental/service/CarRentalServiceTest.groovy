package com.statestreet.car_rental.service

import com.statestreet.car_rental.dto.CarType
import com.statestreet.car_rental.exception.NoAvailableCarException
import com.statestreet.car_rental.repository.RentalRepository
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant
import java.time.temporal.ChronoUnit

class CarRentalServiceTest extends Specification {

    def rentalRepository = Mock(RentalRepository)
    def carSelector = Mock(CarSelector)

    @Subject
    CarRentalService service = new CarRentalService(rentalRepository, carSelector)

    def "should reserve first available candidate"() {
        given:
        def type = CarType.SEDAN
        def startDateTime = Instant.parse("2026-09-20T10:00:00Z")
        def numberOfDays = 3
        def endDateTime = startDateTime.plus(numberOfDays, ChronoUnit.DAYS)
        def candidates = [1L, 2L]

        when:
        service.createReservation(type, startDateTime, numberOfDays)

        then:
        1 * rentalRepository.findAvailableCars(type, startDateTime, endDateTime) >> candidates
        1 * carSelector.selectCandidates(candidates) >> candidates
        1 * rentalRepository.lockCarForUpdate(1L)
        1 * rentalRepository.isCarAvailable(1L, startDateTime, endDateTime)  >> true
        1 * rentalRepository.createReservation(1L, startDateTime, endDateTime)
        0 * rentalRepository.lockCarForUpdate(2L)
        0 * rentalRepository.isCarAvailable(2L, _, _)
        0 * rentalRepository.createReservation(2L, _, _)
    }

    def "should reserve next candidate when first candidate becomes unavailable"() {
        given:
        def type = CarType.SEDAN
        def startDateTime = Instant.parse("2026-09-20T10:00:00Z")
        def numberOfDays = 3
        def endDateTime = startDateTime.plus(numberOfDays, ChronoUnit.DAYS)
        def candidates = [1L, 2L]

        when:
        service.createReservation(type, startDateTime, numberOfDays)

        then:
        1 * rentalRepository.findAvailableCars(type, startDateTime, endDateTime) >> candidates
        1 * carSelector.selectCandidates(candidates) >> candidates
        1 * rentalRepository.lockCarForUpdate(1L)
        1 * rentalRepository.isCarAvailable(1L, startDateTime, endDateTime) >> false
        1 * rentalRepository.lockCarForUpdate(2L)
        1 * rentalRepository.isCarAvailable(2L, startDateTime, endDateTime) >> true
        1 * rentalRepository.createReservation(2L, startDateTime, endDateTime)
        0 * rentalRepository.createReservation(1L, _, _)
    }

    def "should throw NoAvailableCarException when all candidates become unavailable"() {
        given:
        def type = CarType.VAN
        def startDateTime = Instant.parse("2026-09-20T10:00:00Z")
        def numberOfDays = 3
        def endDateTime = startDateTime.plus(numberOfDays, ChronoUnit.DAYS)
        def candidates = [1L, 2L, 3L]

        when:
        service.createReservation(type, startDateTime, numberOfDays)

        then:
        thrown(NoAvailableCarException)

        1 * rentalRepository.findAvailableCars(type, startDateTime, endDateTime) >> candidates
        1 * carSelector.selectCandidates(candidates) >> candidates
        1 * rentalRepository.lockCarForUpdate(1L)
        1 * rentalRepository.isCarAvailable(1L, startDateTime, endDateTime) >> false
        1 * rentalRepository.lockCarForUpdate(2L)
        1 * rentalRepository.isCarAvailable(2L, startDateTime, endDateTime) >> false
        1 * rentalRepository.lockCarForUpdate(3L)
        1 * rentalRepository.isCarAvailable(3L, startDateTime, endDateTime) >> false
        0 * rentalRepository.createReservation(_, _, _)
    }

    def "should throw NoAvailableCarException when no candidates are found"() {
        given:
        def type = CarType.SUV
        def startDateTime = Instant.parse("2026-09-20T10:00:00Z")
        def numberOfDays = 3
        def endDateTime = startDateTime.plus(numberOfDays, ChronoUnit.DAYS)

        when:
        service.createReservation(type, startDateTime, numberOfDays)

        then:
        thrown(NoAvailableCarException)

        1 * rentalRepository.findAvailableCars(type, startDateTime, endDateTime) >> []
        1 * carSelector.selectCandidates([])  >> []
        0 * rentalRepository.lockCarForUpdate(_)
        0 * rentalRepository.isCarAvailable(_, _, _)
        0 * rentalRepository.createReservation(_, _, _)
    }
}
