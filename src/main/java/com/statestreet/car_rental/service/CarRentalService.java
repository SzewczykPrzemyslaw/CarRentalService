package com.statestreet.car_rental.service;

import com.statestreet.car_rental.dto.CarType;
import com.statestreet.car_rental.exception.NoAvailableCarException;
import com.statestreet.car_rental.repository.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CarRentalService {

    private final RentalRepository rentalRepository;
    private final CarSelector carSelectorService;

    public CarRentalService(RentalRepository rentalRepository, CarSelector carSelectorService) {
        this.rentalRepository = rentalRepository;
        this.carSelectorService = carSelectorService;
    }

    @Transactional
    public void createReservation(CarType type, Instant startDateTime, int numberOfDays) {
        Instant endDateTime = startDateTime.plus(numberOfDays, ChronoUnit.DAYS);
        List<Long> candidates = rentalRepository.findAvailableCars(type, startDateTime, endDateTime);
        List<Long> orderedCandidates = carSelectorService.selectCandidates(candidates);

        if (!reserveAvailableCar(orderedCandidates, startDateTime, endDateTime)) {
            throw new NoAvailableCarException(type, startDateTime, endDateTime);
        }
    }

    private boolean reserveAvailableCar(List<Long> candidateIds, Instant startDateTime, Instant endDateTime) {
        for (Long carId : candidateIds) {
            rentalRepository.lockCarForUpdate(carId);
            if (rentalRepository.isCarAvailable(carId, startDateTime, endDateTime)) {
                rentalRepository.createReservation(carId, startDateTime, endDateTime);
                return true;
            }
        }
        return false;
    }

}
