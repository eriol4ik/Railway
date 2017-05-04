package dao;

import domain.entity.Station;
import domain.entity.UnitRoute;

public interface UnitRouteDAO extends DAO<UnitRoute, Long> {
    UnitRoute find(Station startStation, Station endStation);
}
