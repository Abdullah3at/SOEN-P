package persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() throws SQLException {
        // Initialize the database connection
        String url = "jdbc:mysql://sql5.freesqldatabase.com:3306/sql5767928";
        String user = "sql5767928";
        String password = "zLrrGJagjE";
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