package domain.enums;

public enum CarriageType {
    SEATING_FIRST_CLASS("Сидячий 1-го класса"),
    SEATING_SECOND_CLASS("Сидячий 2-го класса"),
    SEATING_THIRD_CLASS("Сидячий 3-го класса"),
    SLEEPER_FIRST_CLASS("Люкс"),
    SLEEPER_SECOND_CLASS("Купе"),
    SLEEPER_THIRD_CLASS("Плацкарт");

    private String label;

    CarriageType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
