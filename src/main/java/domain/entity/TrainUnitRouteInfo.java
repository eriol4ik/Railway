package domain.entity;

import java.time.Period;

// TrainUnitRouteInfo for unit fullRoute between current and
// previous stations which refers to specific train
public class TrainUnitRouteInfo {
    // travel time between current and previous stations
    Integer duration;
    // stop time at the end station of unit fullRoute
    Integer stopTime;

    public TrainUnitRouteInfo() {}

    public TrainUnitRouteInfo(Integer unitRouteDuration, Integer stopTime) {
        this.duration = unitRouteDuration;
        this.stopTime = stopTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getStopTime() {
        return stopTime;
    }

    public void setStopTime(Integer stopTime) {
        this.stopTime = stopTime;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash += duration.hashCode() * 31;
        hash += stopTime.hashCode() * 31;

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        TrainUnitRouteInfo other = (TrainUnitRouteInfo) obj;
        if (duration == null || !duration.equals(other.duration)) return false;

        return stopTime != null && stopTime.equals(other.stopTime);
    }

    @Override
    public String toString() {
        String temp = super.toString();
        temp += "{duration='" + duration;
        temp += ", stopTime='" + stopTime;
        temp += "'}";

        return temp;
    }
}