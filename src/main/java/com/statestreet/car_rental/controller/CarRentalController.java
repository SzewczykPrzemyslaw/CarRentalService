package com.statestreet.car_rental.controller;

import com.statestreet.car_rental.dto.RentCarRequest;
import com.statestreet.car_rental.service.CarRentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class CarRentalController {

    private final CarRentalService service;

    public CarRentalController(CarRentalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> rentCar(@Valid @RequestBody RentCarRequest request) {
        service.createReservation(request.type(), request.startDateTime(), request.numberOfDays());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
