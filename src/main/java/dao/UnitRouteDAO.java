package dao;

import domain.entity.Station;
import domain.entity.UnitRoute;
import exception.PersistentException;

public interface UnitRouteDAO extends DAO<UnitRoute, Long> {
    UnitRoute find(Station startStation, Station endStation) throws PersistentException;
    UnitRoute find(String startStationName, String endStationName) throws PersistentException;
}
