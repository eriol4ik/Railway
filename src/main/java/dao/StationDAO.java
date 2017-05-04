package dao;

import domain.entity.Station;

public interface StationDAO extends DAO<Station, Long> {
    Station find(String name);
}
