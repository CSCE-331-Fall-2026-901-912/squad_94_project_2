package database;

import java.sql.*;
import javax.sql.*;
import org.postgresql.ds.PGSimpleDataSource;

public class PGComms {

    // Define the unique database source this class allows one to refer to.
    private static PGSimpleDataSource source = null;

    // Define a variable to hold the database connection.
    private static Connection conn = null;

    // Set all properties of the database stored in 'source.'
    public static void initialize_database(String server_name, String database_name, String user, String password) {
        source = new PGSimpleDataSource();
        source.setServerNames(new String[]{server_name});
        source.setDatabaseName(database_name);
        source.setUser(user);
        source.setPassword(password);
    }

    // Private helper function to open the connection to the database.
    private static void open_connection() throws SQLException {

        // If a connection to the database has not yet been opened, open it.
        if (conn == null) {
            conn = source.getConnection();
        }
    }

    // Private helper function to close the connection to the database.
    private static void close_connection() throws SQLException {

        // If a connection to the database is currently open, close it.
        if (conn != null) {
            conn.close();
            conn = null;
        }
    }

}