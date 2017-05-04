package domain.entity;

import domain.enum_type.CarriageType;

import java.time.LocalTime;
import java.time.Period;
import java.util.Map;

public class Train {
    // for example '097K'
    private String trainId;

    private LocalTime departureTime;
    private Period duration; // -> arrival time

    private Map<Integer, CarriageType> carriageMap;

    private Map<UnitRoute, TrainUnitRouteInfo> fullRoute;

    public Train() {}

    public Train(String trainId,
                 LocalTime departureTime,
                 Period duration,
                 Map<Integer, CarriageType> carriageMap,
                 Map<UnitRoute, TrainUnitRouteInfo> fullRoute) {
        this.trainId = trainId;
        this.departureTime = departureTime;
        this.duration = duration;
        this.carriageMap = carriageMap;
        this.fullRoute = fullRoute;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public Period getDuration() {
        return duration;
    }

    public void setDuration(Period duration) {
        this.duration = duration;
    }

    public Map<Integer, CarriageType> getCarriageMap() {
        return carriageMap;
    }

    public void setCarriageMap(Map<Integer, CarriageType> carriageMap) {
        this.carriageMap = carriageMap;
    }

    public Map<UnitRoute, TrainUnitRouteInfo> getFullRoute() {
        return fullRoute;
    }

    public void setFullRoute(Map<UnitRoute, TrainUnitRouteInfo> fullRoute) {
        this.fullRoute = fullRoute;
    }
}
