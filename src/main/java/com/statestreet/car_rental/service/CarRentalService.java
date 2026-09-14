package com.statestreet.car_rental.service;

import com.statestreet.car_rental.dto.CarType;
import com.statestreet.car_rental.exception.NoAvailableCarException;
import com.statestreet.car_rental.repository.RentalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CarRentalService {

    private static final Logger log = LoggerFactory.getLogger(CarRentalService.class);
    private final RentalRepository rentalRepository;
    private final CarSelector carSelectorService;

    public CarRentalService(RentalRepository rentalRepository, CarSelector carSelectorService) {
        this.rentalRepository = rentalRepository;
        this.carSelectorService = carSelectorService;
    }

    @Transactional
    public void createReservation(CarType type, Instant startDateTime, int numberOfDays) {
        Instant endDateTime = startDateTime.plus(numberOfDays, ChronoUnit.DAYS);
        log.debug("Creating reservation: type = '{}', startDateTime = '{}', endDateTime = '{}'", type, startDateTime, endDateTime);
        List<Long> candidates = rentalRepository.findAvailableCars(type, startDateTime, endDateTime);
        List<Long> orderedCandidates = carSelectorService.selectCandidates(candidates);

        if (!reserveAvailableCar(orderedCandidates, startDateTime, endDateTime)) {
            throw new NoAvailableCarException(type, startDateTime, endDateTime);
        }
    }

    private boolean reserveAvailableCar(List<Long> candidateIds, Instant startDateTime, Instant endDateTime) {
        for (Long carId : candidateIds) {
            log.debug("Attempting to reserve car: carId = '{}'", carId);
            rentalRepository.lockCarForUpdate(carId);
            if (rentalRepository.isCarAvailable(carId, startDateTime, endDateTime)) {
                rentalRepository.createReservation(carId, startDateTime, endDateTime);
                log.info("Car successfully reserved: carId = '{}', startDateTime = '{}', endDateTime = '{}'", carId, startDateTime, endDateTime);
                return true;
            }
            log.debug("Car is no longer available: carId = '{}'", carId);
        }
        return false;
    }

}
