package dao;

import domain.entities.Station;
import domain.entities.UnitRoute;

public interface UnitRouteDAO extends DAO<UnitRoute, Long> {
    UnitRoute find(Station startStation, Station endStation);
}
