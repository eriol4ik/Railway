package dao;

import domain.entities.Station;
import domain.entities.Train;
import domain.enums.CarriageType;
import domain.enums.PlaceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TrainDAO extends DAO<Train, String> {
    List<Train> find(Station startStation, Station endStation);

    Boolean hasAvailablePlaces(Train train,
                               LocalDate date,
                               Station startStation,
                               Station endStation);

    Map<Integer, Map<Integer, PlaceStatus>> readAvailabilityMap(Train train);
    Map<CarriageType, BigDecimal> readTicketPrices(Train train);
}
