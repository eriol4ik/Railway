package dao;

import domain.entity.Station;
import domain.entity.UnitRoute;
import exception.PersistentException;
import util.EntityHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UnitRouteDAOImpl extends DAOImpl<UnitRoute, Long> implements UnitRouteDAO {
    private final String CREATE_QUERY = "INSERT INTO unit_routes (start_station_id, end_station_id, distance) VALUES (?, ?, ?);";
    private final String SELECT_ID_QUERY = "SELECT LAST_INSERT_ID()";
    private final String READ_QUERY = "SELECT start_station_id, end_station_id, distance FROM unit_routes WHERE unit_route_id = ?";
    private final String UPDATE_QUERY = "UPDATE unit_routes SET start_station_id = ?, end_station_id = ?, distance = ? WHERE unit_route_id = ?";
    private final String DELETE_QUERY = "DELETE FROM unit_routes WHERE unit_route_id = ?";
    private final String FIND_QUERY = "SELECT unit_route_id, distance FROM unit_routes WHERE start_station_id = ? AND end_station_id = ?";

    private static UnitRouteDAO instance;

    private UnitRouteDAOImpl() {}

    public static UnitRouteDAO getInstance() {
        if (instance == null) {
            synchronized (UnitRouteDAOImpl.class) {
                if (instance == null) {
                    instance = new UnitRouteDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    protected Boolean checkForCreate(UnitRoute route) {
        return route.getStart() != null &&
               route.getEnd() != null &&
               route.getDistance() != null;
    }

    @Override
    protected Boolean checkForRead(Long id) {
        return id != null;
    }

    @Override
    protected Boolean checkForUpdate(UnitRoute route) {
        return EntityHelper.hasCompleteInfo(route);
    }

    @Override
    protected Boolean checkForDelete(UnitRoute route) {
        return EntityHelper.hasCompleteInfo(route);
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
    protected Object[] getCreateParameters(UnitRoute route) throws PersistentException {
        Long startStationId = route.getStart().getStationId();
        if (route.getStart().getStationId() == null) {
            startStationId = DAOFactory.getStationDAO().create(route.getStart());
        }
        Long endStationId = route.getEnd().getStationId();
        if (route.getEnd().getStationId() == null) {
            endStationId = DAOFactory.getStationDAO().create(route.getEnd());
        }

        return new Object[]{startStationId, endStationId, route.getDistance()};
    }

    @Override
    protected Object[] getUpdateParameters(UnitRoute route) {
        return new Object[]{route.getStart().getStationId(),
                            route.getEnd().getStationId(),
                            route.getDistance(),
                            route.getRouteId()};
    }

    @Override
    protected Object[] getDeleteParameters(UnitRoute route) {
        return new Object[]{route.getRouteId()};
    }

    @Override
    protected Long parseForCreate(ResultSet resultSet) throws PersistentException {
        try {
            Long routeId = null;
            if (resultSet.next()) {
                routeId = resultSet.getLong(1);
            }

            return routeId;
        } catch (SQLException e) {
            throw new PersistentException(PersistentException.PARSING_ERROR);
        }
    }

    @Override
    protected void setPK(UnitRoute route, Long id) {
        route.setRouteId(id);
    }

    @Override
    protected UnitRoute parseForRead(ResultSet resultSet) throws PersistentException {
        try {
            UnitRoute route = null;
            if (resultSet.next()) {
                StationDAO stationDAO = DAOFactory.getStationDAO();
                stationDAO.setConnection(connection);

                Station start = stationDAO.read(resultSet.getLong(1));
                Station end = stationDAO.read(resultSet.getLong(2));
                Integer distance = resultSet.getInt(3);

                if (start == null || end == null) {
                    return null;
                }

                route = new UnitRoute(start, end, distance);
            }
            return route;
        } catch (SQLException e) {
            throw new PersistentException(PersistentException.PARSING_ERROR);
        }
    }

    @Override
    public UnitRoute find(Station startStation, Station endStation) throws PersistentException {
        if (startStation == null || endStation == null) {
            return null;
        }

        ResultSet resultSet = executeQuery(FIND_QUERY, startStation.getStationId(), endStation.getStationId());

        UnitRoute route = parseForRead(startStation, endStation, resultSet);
        route.setStart(startStation);
        route.setEnd(endStation);

        return route;
    }

    private UnitRoute parseForRead(Station startStation, Station endStation, ResultSet resultSet) throws PersistentException {
        try {
            UnitRoute route = null;
            if (resultSet.next()) {
                route = new UnitRoute();
                route.setRouteId(resultSet.getLong(1));
                route.setDistance(resultSet.getInt(2));
            }

            return route;
        } catch (SQLException e) {
            throw new PersistentException(PersistentException.PARSING_ERROR);
        }
    }

    @Override
    public UnitRoute find(String startStationName, String endStationName) throws PersistentException {
        StationDAO stationDAO = DAOFactory.getStationDAO();
        Station start = stationDAO.find(startStationName);
        Station end = stationDAO.find(endStationName);
        return find(start, end);
    }
}
