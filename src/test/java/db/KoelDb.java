package db;

import java.lang.reflect.InvocationTargetException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoelDb {
    package db;

import org.mariadb.jdbc.Connection;

import java.lang.reflect.InvocationTargetException;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class KoelDb {
        public static Connection connection;
        private static final ThreadLocal<Connection> threadDriver = new ThreadLocal<>();
        public static Connection getDbConnection() {
            return threadDriver.get();
        }

        public static String DB_URL = System.getProperty("dbUrl");
        public static String DB_USER = System.getProperty("dbUser");
        public static String DB_PASS = System.getProperty("dbPassword");
        public static void initializeDb() throws SQLException, ClassNotFoundException {
            threadDriver.set(setupDB());
        }
        public static Connection setupDB() throws SQLException, ClassNotFoundException {
            try {
                Class.forName("org.mariadb.jdbc.Driver").getDeclaredConstructor().newInstance();
                connection = (Connection) DriverManager.getConnection(System.getProperty("dbUrl"), System.getProperty("dbUser"),  System.getProperty("dbPassword"));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Connected to Koel database: " + connection.isValid(0));
            return connection;
        }
        public static void closeDatabaseConnection() throws SQLException {
            if (getDbConnection() != null) {
                getDbConnection().close();
                System.out.println("Connection to Koel database closed: " + getDbConnection().isClosed());
            }
            threadDriver.remove();
        }
    }

}
