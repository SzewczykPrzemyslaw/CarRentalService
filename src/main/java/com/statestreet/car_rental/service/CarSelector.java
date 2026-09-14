package com.statestreet.car_rental.service;

import java.util.List;

public interface CarSelector {

    List<Long> selectCandidates(List<Long> carIds);
}
