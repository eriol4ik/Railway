package dao.pool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private ConnectionPool() {}

    public static Connection getConnection() throws SQLException {
        DataSource dataSource = dao.pool.DataSource.getInstance();

        if (dataSource == null) {
            throw new SQLException("Data Source is unavailable");
        }

        return dataSource.getConnection();
    }
}
