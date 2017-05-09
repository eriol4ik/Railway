package dao;

import domain.entity.Station;
import exception.PersistentException;

public interface StationDAO extends DAO<Station, Long> {
    Station find(String name) throws PersistentException;
}
