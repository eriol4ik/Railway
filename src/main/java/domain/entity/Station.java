package domain.entity;

public class Station {
    private Long stationId;
    private String name;

    public Station() {}

    public Station(String name) {
        this.name = name;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash += stationId != null ? Long.hashCode(stationId) * 31 : 0;
        hash += name != null ? name.hashCode() * 31 : 0;

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        Station other = (Station) obj;
        if (this.stationId == null || !stationId.equals(other.stationId)) return false;

        return name != null && name.equals(other.name);
    }

    @Override
    public String toString() {
        return "Station{@" + super.toString() +
                ", id=" + stationId +
                ", name=" + name +
                "}";
    }
}
