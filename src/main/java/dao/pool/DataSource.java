package dao.pool;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class DataSource {
    private static volatile javax.sql.DataSource dataSource;

    private static final String NAME = "java:comp/env/jdbc/railway";

    private DataSource() {}

    public static javax.sql.DataSource getInstance() {
        if (dataSource == null) {
            synchronized (javax.sql.DataSource.class) {
                if (dataSource == null) {
                    try {
                        InitialContext context = new InitialContext();
                        dataSource = (javax.sql.DataSource) context.lookup(NAME);
                        return dataSource;
                    } catch (NamingException ne) {
                        ne.printStackTrace();
                        return null;
                    }
                }
            }
        }

        return dataSource;
    }
}
