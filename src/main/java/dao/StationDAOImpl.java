package dao;

import domain.entity.Station;
import util.EntityHelper;

import java.sql.*;

public class StationDAOImpl extends DAOImpl<Station, Long> implements StationDAO {
    private final String CREATE_QUERY = "INSERT INTO stations (name) VALUES (?);";
    private final String SELECT_ID_QUERY = "SELECT LAST_INSERT_ID()";
    private final String READ_QUERY = "SELECT name FROM stations WHERE station_id = ?";
    private final String UPDATE_QUERY = "UPDATE stations SET name = ? WHERE station_id = ?";
    private final String DELETE_QUERY = "DELETE FROM stations WHERE station_id = ? AND name = ?";


    @Override
    public Station find(String name) throws SQLException {
        if (name == null || name.isEmpty()) {
            return null;
        }

        initConnection();

        String query = "SELECT station_id, name FROM stations WHERE LOWER(name) = ?";
        ResultSet resultSet = executeQuery(query, name.toLowerCase());

        Station station;
        if (resultSet.next()) {
            station = new Station();
            station.setStationId(resultSet.getLong(1));
            station.setName(resultSet.getString(2));
            return station;
        }

        commitAndClose();

        return null;
    }

    @Override
    protected Boolean checkForCreate(Station station) {
        return station.getName() != null &&
               !station.getName().isEmpty();
    }

    @Override
    protected Boolean checkForRead(Long id) {
        return id != null;
    }

    @Override
    protected Boolean checkForUpdate(Station station) {
        return EntityHelper.hasCompleteInfo(station);
    }

    @Override
    protected Boolean checkForDelete(Station station) {
        return EntityHelper.hasCompleteInfo(station);
    }

    @Override
    protected String getCreateQuery() {
        return CREATE_QUERY;
    }

    @Override
    protected String getSelectIdQuery() {
        return SELECT_ID_QUERY;
    }

    @Override
    protected String getReadQuery() {
        return READ_QUERY;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected String getDeleteQuery() {
        return DELETE_QUERY;
    }

    @Override
    protected Object[] getCreateParameters(Station station) {
        return new Object[]{station.getName()};
    }

    @Override
    protected Object[] getUpdateParameters(Station station) {
        return new Object[]{station.getName(), station.getStationId()};
    }

    @Override
    protected Object[] getDeleteParameters(Station station) {
        return new Object[]{station.getStationId(), station.getName()};
    }

    @Override
    protected Long parseForCreate(ResultSet resultSet) throws SQLException {
        Long stationId = null;
        if (resultSet.next()) {
            stationId = resultSet.getLong(1);
        }
        return stationId;
    }

    @Override
    protected void setPK(Station station, Long id) {
        station.setStationId(id);
    }

    @Override
    protected Station parseForRead(ResultSet resultSet) throws SQLException {
        Station station = null;
        if (resultSet.next()) {
            station = new Station();
            station.setName(resultSet.getString(1));
        }
        return station;
    }
}
