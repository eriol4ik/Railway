package service;

import dao.*;
import domain.entity.Station;
import domain.entity.UnitRoute;
import exception.PersistentException;
import exception.TransactionException;

public class UnitRouteServiceImpl extends ServiceImpl<UnitRoute, Long> implements UnitRouteService {
    private UnitRouteDAO unitRouteDAO;

    public UnitRouteServiceImpl() {
        super(DAOFactory.getUnitRouteDAO());

        unitRouteDAO = DAOFactory.getUnitRouteDAO();
    }

    @Override
    public UnitRoute find(Station startStation, Station endStation) {
        try {
            initConnection(unitRouteDAO);

            UnitRoute unitRoute = unitRouteDAO.find(startStation, endStation);
            commitAndCloseConnection();
            return unitRoute;
        } catch (PersistentException | TransactionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UnitRoute find(String startStationName, String endStationName) {
        try {
            initConnection(unitRouteDAO);

            UnitRoute unitRoute = unitRouteDAO.find(startStationName, endStationName);
            commitAndCloseConnection();
            return unitRoute;
        } catch (PersistentException | TransactionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
