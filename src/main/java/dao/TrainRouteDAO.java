package dao;

import domain.entity.Train;
import domain.entity.TrainUnitRouteInfo;
import domain.entity.UnitRoute;

import java.util.Map;

public interface TrainRouteDAO {
    Map<UnitRoute, TrainUnitRouteInfo> find(Train train);
}
