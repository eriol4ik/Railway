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
import java.util.*;

public class TrainDAOImpl extends DAOImpl<Train, String> implements TrainDAO {


    @Override
    public String create(Train train) throws SQLException {
        if (!checkForCreate(train)) return null;

        initConnection();

        String query   = "INSERT INTO trains (train_id, departure_time, duration) VALUES (?, ?, ?);";
        String query2  = "INSERT INTO carriages (train_id, carriage_number, carriage_type) VALUES (?, ?, ?);";
        String query2a = "SELECT LAST_INSERT_ID()";
        String query3  = "INSERT INTO carriage_places (carriage_id, place_number) VALUES (?, ?)";
        String query4  = "INSERT INTO train_unit_routes (train_id, unit_route_id, previous_unit_route_id, duration, stop_time) VALUES (?, ?, ?, ?, ?)";

        execute(query, train.getTrainId(),
                       Time.valueOf(train.getDepartureTime()),
                       train.getDuration());

        PreparedStatement statement = connection.prepareStatement(query2);
        PreparedStatement statement2 = connection.prepareStatement(query3);
        Map<Integer, CarriageType> carriageMap = train.getCarriageMap();
        for (Map.Entry<Integer, CarriageType> entry : carriageMap.entrySet()) {
            statement.setString(1, train.getTrainId());
            statement.setInt(2, entry.getKey());
            statement.setString(3, entry.getValue().name());
            statement.execute();
            ResultSet resultSet = statement.executeQuery(query2a);
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

        statement = connection.prepareStatement(query4);
        Map<UnitRoute, TrainUnitRouteInfo> fullRouteMap = train.getFullRoute();
        UnitRoute previousUnitRoute = null;
        UnitRouteDAO unitRouteDAO = DAOFactory.getUnitRouteDAO();
        for (Map.Entry<UnitRoute, TrainUnitRouteInfo> fullRouteEntry : fullRouteMap.entrySet()) {
            UnitRoute key = fullRouteEntry.getKey();
            if (key.getRouteId() == null) {
                unitRouteDAO.create(key);
            }
            TrainUnitRouteInfo value = fullRouteEntry.getValue();

            statement.setString(1, train.getTrainId());
            statement.setLong(2, key.getRouteId());
            statement.setObject(3, previousUnitRoute != null ? previousUnitRoute.getRouteId() : null);
            statement.setInt(4, value.getDuration());
            statement.setInt(5, value.getStopTime());
            statement.execute();

            previousUnitRoute = key;
        }

        commitAndClose();

        return null;
    }

    @Override
    public Train read(String id) throws SQLException {
        if (!checkForRead(id)) return null;

        initConnection();

        String mainInfoQuery = "SELECT departure_time, duration FROM trains WHERE train_id = ?";
        String carriageMapQuery = "SELECT carriage_number, carriage_type FROM carriages WHERE train_id = ?";
        String fullRouteQuery = "SELECT previous_unit_route_id, unit_route_id, duration, stop_time FROM train_unit_routes WHERE train_id = ?";

        // get main info
        ResultSet resultSet = executeQuery(mainInfoQuery, id);
        Train train = parseForRead(resultSet);
        if (train == null) return null;
        train.setTrainId(id);

        // get carriage map
        resultSet = executeQuery(carriageMapQuery, id);
        Map<Integer, CarriageType> carriageMap = parseToCarriageMap(resultSet);
        train.setCarriageMap(carriageMap);

        // get full route
        resultSet = executeQuery(fullRouteQuery, id);
        Map<UnitRoute, TrainUnitRouteInfo> fullRoute = parseToFullRoute(resultSet);
        train.setFullRoute(fullRoute);

        commitAndClose();

        return train;
    }

    private Map<UnitRoute, TrainUnitRouteInfo> parseToFullRoute(ResultSet resultSet) throws SQLException {
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
            // Использую тот же индекс, получаем инфу маршрута
            info = infos.get(previousIndex);
            // Добавляем в упорядоченную Мапу
            fullRoute.put(route, info);
        } while(previousRouteIds.indexOf(lastId) != -1);
        return fullRoute;
    }

    private Map<Integer, CarriageType> parseToCarriageMap(ResultSet resultSet) throws SQLException {
        Map<Integer, CarriageType> carriageMap = new TreeMap<>();
        Integer number;
        CarriageType type;
        while (resultSet.next()) {
            number = resultSet.getInt(1);
            type = CarriageType.valueOf(resultSet.getString(2));
            carriageMap.put(number, type);
        }
        return carriageMap;
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
    protected String parseForCreate(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    protected void setPK(Train train, String id) {
        // train has id
    }

    @Override
    protected Train parseForRead(ResultSet resultSet) throws SQLException {
        Train train = null;
        if (resultSet.next()) {
            train = new Train();
            train.setDepartureTime(resultSet.getTime(1).toLocalTime());
            train.setDuration(resultSet.getInt(2));
        }

        return train;
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
