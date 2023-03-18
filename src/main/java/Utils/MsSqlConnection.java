package Utils;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.util.Properties;

public class MsSqlConnection {

    private static MsSqlConnection instance;
    private SQLServerDataSource dataSource;

    private MsSqlConnection() {
        Properties props = PropertiesManager.getProperties();

        String host = props.getProperty("host");
        String database = props.getProperty("database");
        String user = props.getProperty("user");
        String password = props.getProperty("password");

        dataSource = new SQLServerDataSource();
        dataSource.setServerName(host);
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName(database);
        dataSource.setUser(user);
        dataSource.setPassword(password);
    }

    public static synchronized Connection getConnection() throws SQLServerException {
        if (instance == null) {
            instance = new MsSqlConnection();
        }
        return instance.dataSource.getConnection();
    }
}
