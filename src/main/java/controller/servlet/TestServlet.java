package controller.servlet;

import domain.entity.Station;
import domain.entity.Train;
import domain.entity.TrainUnitRouteInfo;
import domain.entity.UnitRoute;
import domain.enum_type.CarriageType;
import service.*;
import util.JSPPaths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

// hardcode tests
@WebServlet(urlPatterns = "/test")
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        test5();
    }

    private void test1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StationService stationService = new StationServiceImpl();
        Station station = new Station("Киев3");
        stationService.create(station);

        Station stationFromDB = stationService.find("Киев3");

        System.out.println(station.equals(stationFromDB));
        System.out.println(station);
        System.out.println(stationFromDB);
        station.setName("Киев5");
        stationService.update(station);

        req.setAttribute("station", station);
        req.getRequestDispatcher(JSPPaths.TEST_PAGE).forward(req, resp);

        try (PrintWriter out = resp.getWriter()) {
            out.println("HELLOOO");
        }
        test2();
    }

    private void test2() {
        UnitRouteService unitRouteService = new UnitRouteServiceImpl();

        Station start = new Station("Киев");
        Station end = new Station("Ровно");
        Integer distance = 38378;

        UnitRoute route = new UnitRoute(start, end, distance);

        unitRouteService.create(route);
        UnitRoute route2 = unitRouteService.read(route.getRouteId());

        System.out.println(route);
        System.out.println(route.hashCode());
        System.out.println(route2);
        System.out.println(route2.hashCode());
        System.out.println(route.equals(route2));

    }

    private void test3(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StationService stationService = new StationServiceImpl();
        UnitRouteService unitRouteService = new UnitRouteServiceImpl();

        UnitRoute route = unitRouteService.find("киев", "ровно");
        System.out.println(route);

        req.setAttribute("route", route);
        req.getRequestDispatcher(JSPPaths.TEST_PAGE).forward(req, resp);
    }

    private void test4() {
        StationService stationService = new StationServiceImpl();
        UnitRouteService unitRouteService = new UnitRouteServiceImpl();
        TrainService trainService = new TrainServiceImpl();

        Station station1 = new Station("Station1");
        Station station2 = new Station("Station2");
        Station station3 = new Station("Station3");
        Station station4 = new Station("Station4");
        Station station5 = new Station("Station5");

        UnitRoute route1 = new UnitRoute(station1, station2, 500);
        UnitRoute route2 = new UnitRoute(station2, station3, 600);
        UnitRoute route3 = new UnitRoute(station3, station4, 700);
        UnitRoute route4 = new UnitRoute(station4, station5, 800);

        Map<Integer, CarriageType> carriageMap = new LinkedHashMap<>();
        carriageMap.put(1, CarriageType.SEATING_1CL);
        carriageMap.put(2, CarriageType.SEATING_1CL);
        carriageMap.put(3, CarriageType.SEATING_2CL);
        carriageMap.put(4, CarriageType.SEATING_2CL);
        carriageMap.put(5, CarriageType.SEATING_2CL);

        Map<UnitRoute, TrainUnitRouteInfo> infoMap = new LinkedHashMap<>();
        TrainUnitRouteInfo info1 = new TrainUnitRouteInfo(20, 2);
        TrainUnitRouteInfo info2 = new TrainUnitRouteInfo(15, 3);
        TrainUnitRouteInfo info3 = new TrainUnitRouteInfo(30, 4);
        TrainUnitRouteInfo info4 = new TrainUnitRouteInfo(40, 4);
        infoMap.put(route1, info1);
        infoMap.put(route2, info2);
        infoMap.put(route3, info3);
        infoMap.put(route4, info4);
        Train train = new Train("800E", LocalTime.of(5, 44), 300, carriageMap, infoMap);
        trainService.create(train);
    }

    private void test5() {
        TrainService trainService = new TrainServiceImpl();
        System.out.println(trainService.read("800E"));
    }
}
