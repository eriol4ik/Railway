package domain.enum_type;

public enum CarriageType {
    SEATING_1CL("Сидячий 1-го класса"),
    SEATING_2CL("Сидячий 2-го класса"),
    SEATING_3CL("Сидячий 3-го класса"),
    SLEEPER_1CL("Люкс"),
    SLEEPER_2CL("Купе"),
    SLEEPER_3CL("Плацкарт");

    private String label;

    CarriageType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
