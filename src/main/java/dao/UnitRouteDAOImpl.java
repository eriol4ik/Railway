package dao;

import dao.pool.ConnectionPool;
import domain.entity.Station;
import domain.entity.UnitRoute;
import util.EntityHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static util.ConnectionManager.close;
import static util.ConnectionManager.rollback;

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
    public void update(UnitRoute entity) {

    }

    @Override
    public void delete(UnitRoute entity) {

    }

    @Override
    public UnitRoute find(Station startStation, Station endStation) {
        return null;
    }
}
