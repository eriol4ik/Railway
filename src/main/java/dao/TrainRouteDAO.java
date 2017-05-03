package dao;

import domain.entities.Train;
import domain.entities.TrainUnitRouteInfo;
import domain.entities.UnitRoute;

import java.util.Map;

public interface TrainRouteDAO {
    Map<UnitRoute, TrainUnitRouteInfo> find(Train train);
}
