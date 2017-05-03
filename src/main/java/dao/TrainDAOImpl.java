package dao;

import dao.pool.ConnectionPool;
import domain.entities.Station;
import domain.entities.Train;
import domain.enums.CarriageType;
import domain.enums.PlaceStatus;
import utils.EntityChecker;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TrainDAOImpl implements TrainDAO {
    private Connection connection;

    @Override
    public void create(Train train) {
        try {
            connection = ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (!EntityChecker.hasCompleteInformation(train)) return;

//        String query = "INSERT INTO trains (train_id, departure_time, duration) ";

//        Statement statement = connection.prepareStatement();
    }

    @Override
    public Train read(String id) {
        return null;
    }

    @Override
    public void update(Train entity) {

    }

    @Override
    public void delete(Train entity) {

    }

    @Override
    public List<Train> find(Station startStation, Station endStation) {
        return null;
    }

    @Override
    public Boolean hasAvailablePlaces(Train train, LocalDate date, Station startStation, Station endStation) {
        return null;
    }

    @Override
    public Map<Integer, Map<Integer, PlaceStatus>> readAvailabilityMap(Train train) {
        return null;
    }

    @Override
    public Map<CarriageType, BigDecimal> readTicketPrices(Train train) {
        return null;
    }
}
