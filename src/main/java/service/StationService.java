package service;

import domain.entity.Station;

public interface StationService extends Service<Station, Long> {
    Station find(String name);
}
