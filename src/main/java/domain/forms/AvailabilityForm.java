package domain.forms;

import domain.entities.Station;
import domain.entities.Train;
import domain.enums.CarriageType;
import domain.enums.PlaceStatus;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

public class AvailabilityForm {
    private Train train;

    // saves statuses for places for a specific date
    // Note that carriageMap must be changed when date is changed
    private Date date;

    private Station from;
    private Station to;

    private Map<Integer, Map<Integer, PlaceStatus>> availabilityMap;
    private Map<CarriageType, BigDecimal> prices;
}
