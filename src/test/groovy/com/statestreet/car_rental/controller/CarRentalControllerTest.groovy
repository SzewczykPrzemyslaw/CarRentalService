package com.statestreet.car_rental.controller

import com.statestreet.car_rental.dto.CarType
import com.statestreet.car_rental.dto.RentCarRequest
import com.statestreet.car_rental.service.CarRentalService
import org.springframework.http.HttpStatus
import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoUnit

class CarRentalControllerTest extends Specification {

    def service = Mock(CarRentalService)
    def controller = new CarRentalController(service)

    def "should create reservation and return CREATED"() {
        given:
        def carType = CarType.SUV
        def startDateTime = Instant.now().plus(1, ChronoUnit.DAYS)
        def numberOfDays = 3
        def rentCarRequest = new RentCarRequest(carType, startDateTime, numberOfDays)

        when:
        def response = controller.rentCar(rentCarRequest)

        then:
        1 * service.createReservation(carType, startDateTime, numberOfDays)
        response.statusCode == HttpStatus.CREATED
    }
}
