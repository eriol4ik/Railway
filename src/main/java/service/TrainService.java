package service;

import domain.entity.Station;
import domain.entity.Train;
import domain.enum_type.CarriageType;
import domain.enum_type.PlaceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TrainService extends Service<Train, String> {
    List<Train> find(Station startStation, Station endStation);

    Boolean hasAvailablePlaces(Train train,
                               LocalDate date,
                               Station startStation,
                               Station endStation);

    Map<Integer, Map<Integer, PlaceStatus>> readAvailabilityMap(Train train);
    Map<CarriageType, BigDecimal> readTicketPrices(Train train);
}
