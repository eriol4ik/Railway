package dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private ConnectionPool() {}

    public static Connection getConnection() {
        DataSource dataSource = dao.DataSource.getInstance();

        if (dataSource == null) {
            return null;
        }

        try {
            return dataSource.getConnection();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }
}
