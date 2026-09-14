package com.statestreet.car_rental.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CarSelectorService implements CarSelector {

    @Override
    public List<Long> selectCandidates(List<Long> carIds) {
        List<Long> candidates = new ArrayList<>(carIds);
        Collections.shuffle(candidates);
        return candidates;
    }
}