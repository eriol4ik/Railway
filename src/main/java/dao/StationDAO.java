package dao;

import domain.entities.Station;

public interface StationDAO extends DAO<Station, Long> {
    Station find(String name);
}
