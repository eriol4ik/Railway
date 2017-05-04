package controller.servlet;

import dao.DAOFactory;
import dao.StationDAO;
import dao.UnitRouteDAO;
import domain.entity.Station;
import domain.entity.UnitRoute;
import util.JSPPaths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// hardcode tests
@WebServlet(urlPatterns = "/test")
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        test2();
    }

    private void test2() {
        UnitRouteDAO unitRouteDAO = DAOFactory.getUnitRouteDAO();

        Station start = new Station("Киев");
        Station end = new Station("Ровно");
        Integer distance = 38378;

        UnitRoute route = new UnitRoute(start, end, distance);

        unitRouteDAO.create(route);
        UnitRoute route2 = unitRouteDAO.read(route.getRouteId());

        System.out.println(route);
        System.out.println(route.hashCode());
        System.out.println(route2);
        System.out.println(route2.hashCode());
        System.out.println(route.equals(route2));
    }

    private void test1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StationDAO stationDAO = DAOFactory.getStationDAO();
        Station station = new Station("Киев3");
        stationDAO.create(station);

        Station stationFromDB = stationDAO.find("Киев3");

        System.out.println(station.equals(stationFromDB));
        System.out.println(station);
        System.out.println(stationFromDB);
        station.setName("Киев5");
        stationDAO.update(station);

        req.setAttribute("station", station);
        req.getRequestDispatcher(JSPPaths.TEST_PAGE).forward(req, resp);

        try(PrintWriter out = resp.getWriter()) {
            out.println("HELLOOO");
        }
    }
}
