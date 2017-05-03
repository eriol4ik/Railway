package dao;

import domain.entities.Station;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;

import static org.junit.Assert.*;

public class StationDAOImplTest {
    StationDAO stationDAO;

    @Before
    public void init() {
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