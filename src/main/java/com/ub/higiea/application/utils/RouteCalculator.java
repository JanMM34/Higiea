package com.ub.higiea.application.utils;

import com.ub.higiea.domain.model.Sensor;

import java.util.List;

public interface RouteCalculator {

    List<Sensor> calculateRoute(List<Sensor> sensors);
}
