package domain.enum_type;

public enum CarriageType {
    SEATING_1CL("Сидячий 1-го класса", 45),
    SEATING_2CL("Сидячий 2-го класса", 60),
    SEATING_3CL("Сидячий 3-го класса", 70),
    SLEEPER_1CL("Люкс", 20),
    SLEEPER_2CL("Купе", 40),
    SLEEPER_3CL("Плацкарт", 60);

    private String label;
    private Integer numberOfPlaces;

    CarriageType(String label, Integer numberOfPlaces) {
        this.label = label;
        this.numberOfPlaces = numberOfPlaces;
    }

    @Override
    public String toString() {
        return label;
    }

    public Integer getNumberOfPlaces() {
        return numberOfPlaces;
    }
}
