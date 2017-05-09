package dao;

import java.sql.Connection;

public class DAOFactory {
    public static StationDAO getStationDAO() {
        return StationDAOImpl.getInstance();
    }

    public static TrainDAO getTrainDAO() {
        return TrainDAOImpl.getInstance();
    }

    public static UnitRouteDAO getUnitRouteDAO() {
        return UnitRouteDAOImpl.getInstance();
    }
}
