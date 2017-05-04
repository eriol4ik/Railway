package domain.entity;

import java.time.Period;

// TrainUnitRouteInfo for unit fullRoute between current and
// previous stations which refers to specific train
public class TrainUnitRouteInfo {
    // travel time between current and previous stations
    Period UnitRouteDuration;
    // stop time at the end station of unit fullRoute
    Period stopTime;
}