package dao;

import domain.entity.Station;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;

import static org.junit.Assert.*;

public class StationDAOImplTest {
    StationDAO stationDAO;

    @Before
    public void init() {
            // Create initial context
            System.clearProperty(Context.INITIAL_CONTEXT_FACTORY);
            System.clearProperty(Context.URL_PKG_PREFIXES);

        stationDAO = DAOFactory.getStationDAO();
        // TODO
    }

    @Test
    public void createAndFind() throws Exception {
        Station station = new Station("Киев");
        stationDAO.create(station);

        Station stationFromDB = stationDAO.find("Киев");

        assertEquals(station, stationFromDB);
    }

}