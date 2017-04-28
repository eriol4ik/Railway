package domain.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "TRAINS")
public class Train {
    private Long id;

    private Map<UnitRoute, Info> route;

    private class Info {
        Integer unitRouteTime;
        Integer stopTime;
        BigDecimal price;
    }
}
