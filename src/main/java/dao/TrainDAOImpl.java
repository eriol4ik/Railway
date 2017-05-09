package dao;

import domain.entity.Station;
import domain.entity.Train;
import domain.entity.TrainUnitRouteInfo;
import domain.entity.UnitRoute;
import domain.enum_type.CarriageType;
import domain.enum_type.PlaceStatus;
import exception.PersistentException;
import util.EntityHelper;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class TrainDAOImpl extends DAOImpl<Train, String> implements TrainDAO {
    public static final String INSERT_MAIN_INFO = "INSERT INTO trains (train_id, departure_time, duration) VALUES (?, ?, ?);";
    public static final String INSERT_CARRIAGE_MAP = "INSERT INTO carriages (train_id, carriage_number, carriage_type) VALUES (?, ?, ?);";
    public static final String SELECT_ID = "SELECT LAST_INSERT_ID()";
    public static final String INSERT_CARRIAGE_PLACES = "INSERT INTO carriage_places (carriage_id, place_number) VALUES (?, ?)";
    public static final String INSERT_TRAIN_ROUTES = "INSERT INTO train_unit_routes (train_id, unit_route_id, previous_unit_route_id, duration, stop_time) VALUES (?, ?, ?, ?, ?)";

    public static final String READ_MAIN_INFO_QUERY = "SELECT departure_time, duration FROM trains WHERE train_id = ?";
    public static final String READ_CARRIAGE_MAP_QUERY = "SELECT carriage_number, carriage_type FROM carriages WHERE train_id = ?";
    public static final String READ_FULL_ROUTE_QUERY = "SELECT previous_unit_route_id, unit_route_id, duration, stop_time FROM train_unit_routes WHERE train_id = ?";

    private static TrainDAO instance;

    private TrainDAOImpl() {}

    public static TrainDAO getInstance() {
        if (instance == null) {
            synchronized (TrainDAOImpl.class) {
                if (instance == null) {
                    instance = new TrainDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public String create(Train train) throws PersistentException {
        if (!checkForCreate(train)) return null;

        String trainId = train.getTrainId();
        Map<Integer, CarriageType> carriageMap = train.getCarriageMap();
        Map<UnitRoute, TrainUnitRouteInfo> fullRouteMap = train.getFullRoute();

        persistMainInfo(trainId,
                        Time.valueOf(train.getDepartureTime()),
                        train.getDuration());

        persistCarriageMap(trainId, carriageMap);

        persistFullRoute(trainId, fullRouteMap);

        return trainId;
    }

    private void persistMainInfo(String trainId, Time departureTime, Integer duration) throws PersistentException {
        execute(INSERT_MAIN_INFO, trainId,
                                  departureTime,
                                  duration);
    }

    private void persistCarriageMap(String trainId, Map<Integer, CarriageType> carriageMap) throws PersistentException {
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_CARRIAGE_MAP);
            PreparedStatement statement2 = connection.prepareStatement(INSERT_CARRIAGE_PLACES);

            for (Map.Entry<Integer, CarriageType> entry : carriageMap.entrySet()) {
                statement.setString(1, trainId);
                statement.setInt(2, entry.getKey());
                statement.setString(3, entry.getValue().name());
                statement.execute();
                ResultSet resultSet = statement.executeQuery(SELECT_ID);
                Long id = null;
                if (resultSet.next()) {
                    id = resultSet.getLong(1);
                }
                for (int placeNumber = 1; placeNumber <= entry.getValue().getNumberOfPlaces(); placeNumber++) {
                    statement2.setObject(1, id);
                    statement2.setInt(2, placeNumber);
                    statement2.execute();
                }
            }
        } catch (SQLException e) {
            throw new PersistentException(PersistentException.CREATE_CARRIAGE_MAP_ERROR, e);
        }
    }

    private void persistFullRoute(String trainId, Map<UnitRoute, TrainUnitRouteInfo> fullRouteMap) throws PersistentException {
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_TRAIN_ROUTES);

            UnitRoute previousUnitRoute = null;
            UnitRouteDAO unitRouteDAO = DAOFactory.getUnitRouteDAO();
            unitRouteDAO.setConnection(connection);
            for (Map.Entry<UnitRoute, TrainUnitRouteInfo> fullRouteEntry : fullRouteMap.entrySet()) {
                UnitRoute key = fullRouteEntry.getKey();
                if (key.getRouteId() == null) {
                    unitRouteDAO.create(key);
                }
                TrainUnitRouteInfo value = fullRouteEntry.getValue();

                statement.setString(1, trainId);
                statement.setLong(2, key.getRouteId());
                statement.setObject(3, previousUnitRoute != null ? previousUnitRoute.getRouteId() : null);
                statement.setInt(4, value.getDuration());
                statement.setInt(5, value.getStopTime());
                statement.execute();

                previousUnitRoute = key;
            }
        } catch (SQLException e) {
            throw new PersistentException(PersistentException.CREATE_FULL_ROUTE_ERROR, e);
        }
    }

    @Override
    public Train read(String id) throws PersistentException {
        if (!checkForRead(id)) return null;
        
        // get main info
        ResultSet resultSet = executeQuery(READ_MAIN_INFO_QUERY, id);
        Train train = parseForRead(resultSet);
        if (train == null) return null;
        train.setTrainId(id);

        // get carriage map
        resultSet = executeQuery(READ_CARRIAGE_MAP_QUERY, id);
        Map<Integer, CarriageType> carriageMap = parseToCarriageMap(resultSet);
        train.setCarriageMap(carriageMap);

        // get full route
        resultSet = executeQuery(READ_FULL_ROUTE_QUERY, id);
        Map<UnitRoute, TrainUnitRouteInfo> fullRoute = parseToFullRoute(resultSet);
        train.setFullRoute(fullRoute);

        return train;
    }

    private Map<UnitRoute, TrainUnitRouteInfo> parseToFullRoute(ResultSet resultSet) throws PersistentException {
        try {
            List<Long> currentRouteIds = new ArrayList<>();
            List<Long> previousRouteIds = new ArrayList<>();
            List<TrainUnitRouteInfo> infos = new ArrayList<>();

            TrainUnitRouteInfo info;

            while (resultSet.next()) {
                previousRouteIds.add(resultSet.getLong(1));
                currentRouteIds.add(resultSet.getLong(2));
                info = new TrainUnitRouteInfo(resultSet.getInt(3),
                        resultSet.getInt(4));
                infos.add(info);
            }

            Map<UnitRoute, TrainUnitRouteInfo> fullRoute = new LinkedHashMap<>();

            UnitRouteDAO unitRouteDAO = DAOFactory.getUnitRouteDAO();
            unitRouteDAO.setConnection(connection);
            UnitRoute route;

            int previousIndex;
            long lastId = 0L;
            do {
                // Ищем начало маршрута (в БД previous_unit_route_id = null, в ResultSet - 0)
                previousIndex = previousRouteIds.indexOf(lastId);
                // Получаем id текущего единичного маршрута (который == lastId),
                // используя предыдущий (все списки упорядочены согласно БД)
                lastId = currentRouteIds.get(previousIndex);
                // Получаем маршрут из БД
                route = unitRouteDAO.read(lastId);
                // Используя тот же индекс, получаем инфу маршрута
                info = infos.get(previousIndex);
                // Добавляем в упорядоченную Мапу
                fullRoute.put(route, info);
            } while (previousRouteIds.indexOf(lastId) != -1);
            return fullRoute;
        } catch (SQLException e) {
            throw new PersistentException(PersistentException.PARSING_ERROR, e);
        }
    }

    private Map<Integer, CarriageType> parseToCarriageMap(ResultSet resultSet) throws PersistentException {
        try {
            Map<Integer, CarriageType> carriageMap = new TreeMap<>();
            Integer number;
            CarriageType type;
            while (resultSet.next()) {
                number = resultSet.getInt(1);
                type = CarriageType.valueOf(resultSet.getString(2));
                carriageMap.put(number, type);
            }
            return carriageMap;
        } catch (SQLException e) {
            throw new PersistentException(PersistentException.PARSING_ERROR, e);
        }
    }

    @Override
    public void update(Train train) {

    }

    @Override
    public void delete(Train train) {

    }

    @Override
    protected Boolean checkForCreate(Train train) {
        return EntityHelper.hasCompleteInfo(train);
    }

    @Override
    protected Boolean checkForRead(String id) {
        return id != null && id.length() == 4;
    }

    @Override
    protected Boolean checkForUpdate(Train train) {
        return EntityHelper.hasCompleteInfo(train);
    }

    @Override
    protected Boolean checkForDelete(Train train) {
        return EntityHelper.hasCompleteInfo(train);
    }

    @Override
    protected String getCreateQuery() {
        return null;
    }

    @Override
    protected String getSelectIdQuery() {
        return null;
    }

    @Override
    protected String getReadQuery() {
        return null;
    }

    @Override
    protected String getUpdateQuery() {
        return null;
    }

    @Override
    protected String getDeleteQuery() {
        return null;
    }

    @Override
    protected Object[] getCreateParameters(Train entity) {
        return new Object[0];
    }

    @Override
    protected Object[] getUpdateParameters(Train entity) {
        return new Object[0];
    }

    @Override
    protected Object[] getDeleteParameters(Train entity) {
        return new Object[0];
    }

    @Override
    protected String parseForCreate(ResultSet resultSet) throws PersistentException {
        return null;
    }

    @Override
    protected void setPK(Train train, String id) {
        // train has id
    }

    @Override
    protected Train parseForRead(ResultSet resultSet) throws PersistentException {
        try {
            Train train = null;
            if (resultSet.next()) {
                train = new Train();
                train.setDepartureTime(resultSet.getTime(1).toLocalTime());
                train.setDuration(resultSet.getInt(2));
            }

            return train;
        } catch (SQLException e) {
            throw new PersistentException(PersistentException.PARSING_ERROR, e);
        }
    }

    @Override
    public List<Train> find(Station startStation, Station endStation) throws PersistentException {
        return null;
    }

    @Override
    public Boolean hasAvailablePlaces(Train train, LocalDate date, Station startStation, Station endStation) throws PersistentException {
        return null;
    }

    @Override
    public Map<Integer, Map<Integer, PlaceStatus>> readAvailabilityMap(Train train) throws PersistentException {
        return null;
    }

    @Override
    public Map<CarriageType, BigDecimal> readTicketPrices(Train train) throws PersistentException {
        return null;
    }
}
