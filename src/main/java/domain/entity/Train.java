package domain.entity;

import domain.enum_type.CarriageType;

import java.time.LocalTime;
import java.time.Period;
import java.util.Map;

public class Train {
    // for example '097K'
    private String trainId;

    private LocalTime departureTime;
    private Integer duration; // -> arrival time

    private Map<Integer, CarriageType> carriageMap;

    private Map<UnitRoute, TrainUnitRouteInfo> fullRoute;

    public Train() {}

    public Train(String trainId,
                 LocalTime departureTime,
                 Integer duration,
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString())
                .append("{")
                .append("id=")
                .append(trainId)
                .append(", departureTime=")
                .append(departureTime)
                .append(", duration=")
                .append(duration)
                .append(", carriageMap{");
        for (Map.Entry<Integer, CarriageType> entry : carriageMap.entrySet()) {
            sb.append(entry.getKey())
                    .append(":")
                    .append(entry.getValue())
                    .append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        sb.append(", fullRoute{");
        for (Map.Entry<UnitRoute, TrainUnitRouteInfo> entry : fullRoute.entrySet()) {
            sb.append(entry.getKey())
                    .append(":")
                    .append(entry.getValue())
                    .append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");

        return sb.toString();
    }
}
