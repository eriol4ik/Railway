package dao;

import dao.pool.ConnectionPool;
import domain.entity.Station;
import domain.entity.UnitRoute;
import util.EntityHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static util.ConnectionManager.*;

public class UnitRouteDAOImpl implements UnitRouteDAO {
    private Connection connection;

    @Override
    public Long create(UnitRoute route) {
        if (route.getStart() == null ||
            route.getEnd() == null ||
            route.getDistance() == null) {
            return null;
        }

        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String query = "INSERT INTO routes (start_station_id, end_station_id, distance) VALUES (?, ?, ?)";
        String query2 = "SELECT LAST_INSERT_ID()";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            Long startStationId = route.getStart().getStationId();
            if (route.getStart().getStationId() == null) {
                startStationId = DAOFactory.getStationDAO().create(route.getStart());
            }
            statement.setLong(1, startStationId);

            Long endStationId = route.getEnd().getStationId();
            if (route.getEnd().getStationId() == null) {
                endStationId = DAOFactory.getStationDAO().create(route.getEnd());
            }
            statement.setLong(2, endStationId);

            statement.setInt(3, route.getDistance());

            statement.execute();
            ResultSet resultSet = statement.executeQuery(query2);
            Long routeId = null;
            if (resultSet.next()) {
                routeId = resultSet.getLong(1);
            }
            route.setRouteId(routeId);
            connection.commit();
            return routeId;
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(connection);
        } finally {
            close(connection);
        }

        return null;
    }

    @Override
    public UnitRoute read(Long id) {
        if (id == null) return null;

        try {
            connection = ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String query = "SELECT start_station_id, end_station_id, distance FROM routes WHERE route_id = ?";
        ResultSet resultSet;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            UnitRoute route = EntityHelper.genetateUnitRouteFromResultSet(resultSet);
            route.setRouteId(id);
            return route;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(connection);
        }
    }

    @Override
    public void update(UnitRoute route) {
        if (EntityHelper.hasCompleteInfo(route)) {
            return;
        }

        try {
            connection = ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String query = "UPDATE routes SET start_station_id = ?, end_station_id = ?, distance = ? WHERE route_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, route.getStart().getStationId());
            preparedStatement.setLong(2, route.getEnd().getStationId());
            preparedStatement.setInt(3, route.getDistance());
            preparedStatement.setLong(4, route.getRouteId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(connection);
        } finally {
            close(connection);
        }
    }

    @Override
    public void delete(UnitRoute route) {
        if (EntityHelper.hasCompleteInfo(route)) {
            return;
        }

        try {
            connection = ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String query = "DELETE FROM routes WHERE route_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, route.getRouteId());
            preparedStatement.execute();
            route.setRouteId(null);
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(connection);
        } finally {
            close(connection);
        }

    }

    @Override
    public UnitRoute find(Station startStation, Station endStation) {
        if (startStation == null || endStation == null) {
            return null;
        }

        try {
            connection = ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String query = "SELECT route_id, distance FROM routes WHERE start_station_id = ? AND end_station_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, startStation.getStationId());
            preparedStatement.setLong(2, endStation.getStationId());
            ResultSet resultSet = preparedStatement.executeQuery();
            UnitRoute route;
            if (resultSet.next()) {
                route = new UnitRoute();
                route.setRouteId(resultSet.getLong(1));
                route.setStart(startStation);
                route.setEnd(endStation);
                route.setDistance(resultSet.getInt(2));
                return route;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }

        return null;
    }

    @Override
    public UnitRoute find(String startStationName, String endStationName) {
        StationDAO stationDAO = DAOFactory.getStationDAO();
        Station start = stationDAO.find(startStationName);
        Station end = stationDAO.find(endStationName);
        return find(start, end);
    }
}
