package persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() throws SQLException {
        // Initialize the database connection i put it localhost
        String url = "jdbc:mysql://cardealershipapp_victorysee:82fec7d831f5d0ade1d73fb7b6bab4abcee88517@xyq-z.h.filess.io:61002/cardealershipapp_victorysee";
        String user = "cardealershipapp_victorysee";
        String password = "82fec7d831f5d0ade1d73fb7b6bab4abcee88517";
        connection = DriverManager.getConnection(url, user, password);
    }

    public int runInsert(String query) throws SQLException {
        Statement statement = connection.createStatement();
        int rowsAffected = statement.executeUpdate(query);
        statement.close();
        return rowsAffected;
    }

    public ResultSet runQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}