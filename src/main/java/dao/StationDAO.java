package dao;

import domain.entity.Station;

import java.sql.SQLException;

public interface StationDAO extends DAO<Station, Long> {
    Station find(String name) throws SQLException;
}
