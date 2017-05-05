package dao;

import dao.pool.ConnectionPool;
import domain.entity.Station;
import domain.entity.Train;
import domain.entity.TrainUnitRouteInfo;
import domain.entity.UnitRoute;
import domain.enum_type.CarriageType;
import domain.enum_type.PlaceStatus;
import util.EntityHelper;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static util.ConnectionManager.*;

public class TrainDAOImpl implements TrainDAO {
    private Connection connection;

    @Override
    public String create(Train train) {
        if (!EntityHelper.hasCompleteInfo(train)) return null;

        try {
            connection = ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String query = "INSERT INTO trains (train_id, departure_time, duration) ";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
        } catch (SQLException e) {
            rollback(connection);
            e.printStackTrace();
        } finally {
            close(connection);
        }

        return null;
    }

    @Override
    public Train read(String id) {
        if (id == null || id.length() != 4) return null;

        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String mainInfoQuery = "SELECT departure_time, duration FROM trains WHERE train_id = ?";
        String carriageMapQuery = "SELECT carriage_number, carriage_type FROM carriages WHERE train_id = ?";
        String fullRouteQuery = "SELECT unit_route_id, duration, stop_time FROM train_routes WHERE train_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(mainInfoQuery);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            Train train = null;
            if (resultSet.next()) {
                train = new Train();
                train.setTrainId(id);
                train.setDepartureTime(resultSet.getTime(1).toLocalTime());
                train.setDuration(resultSet.getInt(2));
            }

            if (train == null) return null;

            statement = connection.prepareStatement(carriageMapQuery);
            statement.setString(1, id);
            resultSet = statement.executeQuery();
            Map<Integer, CarriageType> carriageMap = new TreeMap<>();
            Integer number;
            CarriageType type;
            while (resultSet.next()) {
                number = resultSet.getInt(1);
                type = CarriageType.valueOf(resultSet.getString(2));
                carriageMap.put(number, type);
            }
            train.setCarriageMap(carriageMap);

            if (carriageMap.isEmpty()) return null;

            statement = connection.prepareStatement(fullRouteQuery);
            statement.setString(1, id);
            resultSet = statement.executeQuery();

            Map<UnitRoute, TrainUnitRouteInfo> fullRoute = new LinkedHashMap<>();
            UnitRoute route;
            TrainUnitRouteInfo info;
            UnitRouteDAO unitRouteDAO = DAOFactory.getUnitRouteDAO();
            while (resultSet.next()) {
                // todo
                /*route = unitRouteDAO.read(resultSet.getLong(1));
                info = new TrainUnitRouteInfo(resultSet.getInt(2),
                                              resultSet.getInt(3));
                fullRoute.put(route, info);
                List<UnitRoute> routes = new LinkedList<>();*/
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }

        return null;
    }

    @Override
    public void update(Train train) {

    }

    @Override
    public void delete(Train train) {

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
