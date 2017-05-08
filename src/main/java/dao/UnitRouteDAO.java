package dao;

import domain.entity.Station;
import domain.entity.UnitRoute;

import java.sql.SQLException;

public interface UnitRouteDAO extends DAO<UnitRoute, Long> {
    UnitRoute find(Station startStation, Station endStation) throws SQLException;
    UnitRoute find(String startStationName, String endStationName) throws SQLException;
}
