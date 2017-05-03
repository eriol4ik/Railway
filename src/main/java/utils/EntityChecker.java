package utils;

import domain.entities.Train;
import domain.entities.TrainUnitRouteInfo;
import domain.entities.UnitRoute;
import domain.enums.CarriageType;

import java.util.Map;

public class EntityChecker {
    public static Boolean hasCompleteInformation(Train train) {
        String trainId = train.getTrainId();
        if (trainId == null || trainId.length() != 4) return false;

        if (train.getDepartureTime() == null) return false;

        if (train.getDuration() == null) return false;

        Map<Integer, CarriageType> carriageMap = train.getCarriageMap();
        if (carriageMap == null || carriageMap.isEmpty()) return false;

        Map<UnitRoute, TrainUnitRouteInfo> fullRoute = train.getFullRoute();
        return fullRoute != null && !fullRoute.isEmpty();
    }
}
