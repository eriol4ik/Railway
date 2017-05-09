package dao;

import domain.entity.Station;
import domain.entity.Train;
import domain.enum_type.CarriageType;
import domain.enum_type.PlaceStatus;
import exception.PersistentException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TrainDAO extends DAO<Train, String> {
    List<Train> find(Station startStation, Station endStation) throws PersistentException;

    Boolean hasAvailablePlaces(Train train,
                               LocalDate date,
                               Station startStation,
                               Station endStation) throws PersistentException;

    Map<Integer, Map<Integer, PlaceStatus>> readAvailabilityMap(Train train) throws PersistentException;
    Map<CarriageType, BigDecimal> readTicketPrices(Train train) throws PersistentException;


}
