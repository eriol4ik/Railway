package util;

import dao.DAOFactory;
import dao.StationDAO;
import domain.entity.Station;
import domain.entity.Train;
import domain.entity.TrainUnitRouteInfo;
import domain.entity.UnitRoute;
import domain.enum_type.CarriageType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class EntityHelper {
    public static Boolean hasCompleteInfo(Train train) {
        String trainId = train.getTrainId();
        if (trainId == null || trainId.length() != 4) return false;

        if (train.getDepartureTime() == null) return false;

        if (train.getDuration() == null) return false;

        Map<Integer, CarriageType> carriageMap = train.getCarriageMap();
        if (carriageMap == null || carriageMap.isEmpty()) return false;

        Map<UnitRoute, TrainUnitRouteInfo> fullRoute = train.getFullRoute();
        return fullRoute != null && !fullRoute.isEmpty();
    }

    public static Boolean hasCompleteInfo(Station station) {
        return station.getStationId() != null &&
               station.getName() != null &&
               !station.getName().isEmpty();
    }

    public static Boolean hasCompleteInfo(UnitRoute route) {
        return route.getRouteId() != null &&
               route.getStart() != null &&
               route.getEnd() != null &&
               route.getDistance() != null;
    }

    public static Station generateStationFromResultSet(ResultSet resultSet) throws SQLException {
        Station station;
        if (resultSet.next()) {
            station = new Station();
            station.setName(resultSet.getString(1));
            return station;
        }
        return null;
    }

    public static UnitRoute genetateUnitRouteFromResultSet(ResultSet resultSet) throws SQLException {
        UnitRoute route = null;
        if (resultSet.next()) {
            StationDAO stationDAO = DAOFactory.getStationDAO();

            Station start = stationDAO.read(resultSet.getLong(1));
            Station end = stationDAO.read(resultSet.getLong(2));
            Integer distance = resultSet.getInt(3);

            if (start == null || end == null) {
                return null;
            }

            route = new UnitRoute(start, end, distance);
        }
        return route;
    }
}
