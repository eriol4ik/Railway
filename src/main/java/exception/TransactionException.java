package exception;

public class TransactionException extends Exception {
    public static final String COMMIT_ERROR = "JDBC commit failed";
    public static final String INIT_CONN_ERROR = "Initializing connection failed";
    public static final String ROLLBACK_ERROR = "JDBC rollback failed";
    public static final String CLOSE_CONN_ERROR ="Closing connection failed";

    public TransactionException() {}

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }
}
