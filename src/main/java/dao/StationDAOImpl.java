package dao;

import dao.pool.ConnectionPool;
import domain.entities.Station;

import java.sql.*;

import static utils.ConnectionManager.*;

public class StationDAOImpl implements StationDAO {
    private Connection connection;

    @Override
    public void create(Station station) {
        try {
            connection = ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String query = "INSERT INTO stations (name) VALUES (?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, station.getName());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
    }

    @Override
    public Station read(Long id) {
        return null;
    }

    @Override
    public void update(Station entity) {

    }

    @Override
    public void delete(Station entity) {

    }

    @Override
    public Station find(String name) {
        try {
            connection = ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String query = "SELECT station_id, name FROM stations WHERE name = ?";
        ResultSet resultSet;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            resultSet = statement.executeQuery();

            Station station;
            if (resultSet.next()) {
                station = new Station();
                station.setStationId(resultSet.getLong("station_id"));
                station.setName(resultSet.getString("name"));
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
