package service;

import domain.entity.Station;
import domain.entity.UnitRoute;

public interface UnitRouteService extends Service<UnitRoute, Long> {
    UnitRoute find(Station startStation, Station endStation);
    UnitRoute find(String startStationName, String endStationName);
}
