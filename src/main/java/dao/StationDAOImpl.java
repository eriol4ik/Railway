package dao;

import dao.pool.ConnectionPool;
import domain.entity.Station;
import util.EntityHelper;

import java.sql.*;

import static util.ConnectionManager.*;

public class StationDAOImpl implements StationDAO {
    private Connection connection;

    @Override
    public Long create(Station station) {
        if (station.getName() == null ||
            station.getName().isEmpty()) {
            return null;
        }

        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String insertQuery = "INSERT INTO stations (name) VALUES (?)";
        String selectIdQuery = "SELECT LAST_INSERT_ID()";

        try {
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, station.getName());
            statement.execute();
            ResultSet resultSet = statement.executeQuery(selectIdQuery);
            Long stationId = null;
            if (resultSet.next()) {
                stationId = resultSet.getLong(1);
            }
            station.setStationId(stationId);
            connection.commit();
            return stationId;
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(connection);
        } finally {
            close(connection);
        }

        return null;
    }

    @Override
    public Station read(Long id) {
        if (id == null) return null;

        try {
            connection = ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String query = "SELECT name FROM stations WHERE station_id = ?";
        ResultSet resultSet;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            Station station = EntityHelper.generateStationFromResultSet(resultSet);
            station.setStationId(id);
            return station;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(connection);
        }
    }

    @Override
    public void update(Station station) {
        if (EntityHelper.hasCompleteInfo(station)) {
            return;
        }

        try {
            connection = ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String query = "UPDATE stations SET name = ? WHERE station_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, station.getName());
            preparedStatement.setLong(2, station.getStationId());
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(connection);
        } finally {
            close(connection);
        }
    }

    @Override
    public void delete(Station station) {
        if (EntityHelper.hasCompleteInfo(station)) {
            return;
        }

        try {
            connection = ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String query = "DELETE FROM stations WHERE station_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, station.getStationId());
            preparedStatement.execute();
            station.setStationId(null);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(connection);
        } finally {
            close(connection);
        }
    }

    @Override
    public Station find(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        try {
            connection = ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String query = "SELECT station_id, name FROM stations WHERE LOWER(name) = ?";
        ResultSet resultSet;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name.toLowerCase());
            resultSet = statement.executeQuery();

            Station station;
            if (resultSet.next()) {
                station = new Station();
                station.setStationId(resultSet.getLong(1));
                station.setName(resultSet.getString(2));
                return station;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
        return null;
    }
}
