package dao;

public class DAOFactory {
    public static StationDAO getStationDAO() {
        return new StationDAOImpl();
    }

    public static TrainDAO getTrainDAO() {
        return new TrainDAOImpl();
    }
}
