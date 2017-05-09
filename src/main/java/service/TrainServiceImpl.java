package service;

import dao.*;
import domain.entity.Station;
import domain.entity.Train;
import domain.enum_type.CarriageType;
import domain.enum_type.PlaceStatus;
import exception.PersistentException;
import exception.TransactionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TrainServiceImpl extends ServiceImpl<Train, String> implements TrainService {
    private TrainDAO trainDAO;

    public TrainServiceImpl() {
        super(DAOFactory.getTrainDAO());

        trainDAO = DAOFactory.getTrainDAO();
    }

    @Override
    public List<Train> find(Station startStation, Station endStation) {
        try {
            initConnection(trainDAO);

            List<Train> trains = trainDAO.find(startStation, endStation);
            commitAndCloseConnection();
            return trains;
        } catch (PersistentException | TransactionException e) {
            e.printStackTrace();
            return new ArrayList<>(0);
        }
    }

    @Override
    public Boolean hasAvailablePlaces(Train train, LocalDate date, Station startStation, Station endStation) {
        try {
            initConnection(trainDAO);

            Boolean bool = trainDAO.hasAvailablePlaces(train, date, startStation, endStation);
            commitAndCloseConnection();
            return bool;
        } catch (PersistentException | TransactionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<Integer, Map<Integer, PlaceStatus>> readAvailabilityMap(Train train) {
        try {
            initConnection(trainDAO);

            Map<Integer, Map<Integer, PlaceStatus>> map = trainDAO.readAvailabilityMap(train);
            commitAndCloseConnection();
            return map;
        } catch (PersistentException | TransactionException e) {
            e.printStackTrace();
            return new LinkedHashMap<>(0);
        }
    }

    @Override
    public Map<CarriageType, BigDecimal> readTicketPrices(Train train) {
        try {
            initConnection(trainDAO);

            Map<CarriageType, BigDecimal> map = trainDAO.readTicketPrices(train);
            commitAndCloseConnection();
            return map;
        } catch (PersistentException | TransactionException e) {
            e.printStackTrace();
            return new LinkedHashMap<>(0);
        }
    }
}
