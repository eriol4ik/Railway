package domain.entities;

import domain.enums.CarriageType;

import java.time.LocalTime;
import java.time.Period;
import java.util.Map;

public class Train {
    // for example '097K'
    private String trainId;

    private LocalTime departureTime;
    private Period duration; // -> arrival time

    private Map<Integer, CarriageType> carriageMap;

    private Map<UnitRoute, TrainUnitRouteInfo> fullRoute;


}
