package service;

import dao.*;
import domain.entity.Station;
import exception.PersistentException;
import exception.TransactionException;

public class StationServiceImpl extends ServiceImpl<Station, Long> implements StationService {
    private StationDAO stationDAO;

    public StationServiceImpl() {
        super(DAOFactory.getStationDAO());

        stationDAO = DAOFactory.getStationDAO();
    }

    @Override
    public Station find(String name) {
        try {
            initConnection(stationDAO);

            Station station = stationDAO.find(name);

            commitAndCloseConnection();

            return station;
        } catch (PersistentException | TransactionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
