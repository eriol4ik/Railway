package domain.entity;

public class UnitRoute {
    private Long routeId;
    private Station start;
    private Station end;
    private Integer distance;

    public UnitRoute() {}

    public UnitRoute(Station start, Station end, Integer distance) {
        this.start = start;
        this.end = end;
        this.distance = distance;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Station getStart() {
        return start;
    }

    public void setStart(Station start) {
        this.start = start;
    }

    public Station getEnd() {
        return end;
    }

    public void setEnd(Station end) {
        this.end = end;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        UnitRoute other = (UnitRoute) obj;
        if (routeId == null || !routeId.equals(other.routeId)) return false;
        if (start == null || !start.equals(other.start)) return false;
        if (distance == null || !distance.equals(other.distance)) return false;

        return end != null && end.equals(other.end);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash += routeId != null ? Long.hashCode(routeId) * 31 : 0;
        hash += start.hashCode() * 31;
        hash += end.hashCode() * 31;
        hash += Integer.hashCode(distance) * 31;

        return hash;
    }

    @Override
    public String toString() {
        String temp = super.toString();
        temp += "{id=" + routeId;
        temp += ", start=" + start;
        temp += ", end=" + end;
        temp += ", distance='" + distance;
        temp += "'}";

        return temp;
    }
}
