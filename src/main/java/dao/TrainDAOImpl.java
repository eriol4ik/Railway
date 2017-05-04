package dao;

import dao.pool.ConnectionPool;
import domain.entity.Station;
import domain.entity.Train;
import domain.enum_type.CarriageType;
import domain.enum_type.PlaceStatus;
import util.EntityHelper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TrainDAOImpl implements TrainDAO {
    private Connection connection;

    @Override
    public String create(Train train) {
        try {
            connection = ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        if (!EntityHelper.hasCompleteInfo(train)) return null;

//        String query = "INSERT INTO trains (train_id, departure_time, duration) ";

//        Statement statement = connection.prepareStatement();
        return null;
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
