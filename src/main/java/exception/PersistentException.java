package exception;

public class PersistentException extends Exception {
    public static final String PARSING_ERROR = "Data parsing failed";
    public static final String QUERY_ERROR = "Executing query failed";
    public static final String CREATE_CARRIAGE_MAP_ERROR = "Creating carriage map failed";
    public static final String CREATE_FULL_ROUTE_ERROR = "Creating full route failed";

    public PersistentException() {}

    public PersistentException(String message) {
        super(message);
    }

    public PersistentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistentException(Throwable cause) {
        super(cause);
    }
}
