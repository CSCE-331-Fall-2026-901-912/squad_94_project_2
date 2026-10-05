package database;

import java.sql.*;
import javax.sql.*;
import org.postgresql.ds.PGSimpleDataSource;

public class PGComms {


    // Define the unique database source this class allows one to refer to.
    private static PGSimpleDataSource source = null;

    // Define a variable to hold the database connection.
    private static Connection conn = null;

    /**
     * Set all required attributes necessary for PGComms to manage connections to a database.
     * @param server_name The name of the server hosting the database, likely an IP or similar alias.
     * @param database_name The name of the database that PGComms should enable one to connect to.
     * @param user The name of the user PGComms will manage the database connection for.
     * @param password The password to the database.
     */
    public static void initialize_database(String server_name, String database_name, String user, String password) {
        source = new PGSimpleDataSource();
        source.setServerNames(new String[]{server_name});
        source.setDatabaseName(database_name);
        source.setUser(user);
        source.setPassword(password);
    }

    /**
     * Returns a boolean which indicates if a successful connection was opened, then closed, to the stored database.
     * @return true if the connection was opened, then closed. false if the connection could not be opened and/or closed.
     */
    public static boolean test_connection() {
        boolean can_open = false;
        can_open = open_connection();
        if (!can_open) {
            return false;
        }
        boolean can_close = false;
        can_close = close_connection();
        if (!can_close) {
            return false;
        }
        return true;
    }

    /**
     * THIS FUNCTION DOES NOT SANITIZE QUERIES!
     * Issues the query specified as a parameter to the database.
     * Queries here are "updates." These queries *change* data within the database.
     * A boolean is returned which indicates the success status of the query.
     * Update Queries include: INSERT, UPDATE, DELETE.
     * @param query_type_update The "update" query (String) to be sent to the database.
     * @return true if the update query was successfully processed. false if it was not.
     */
    public static boolean issue_query_type_update(String query_type_update) {
        return true;
    }

    // Private helper function to open the connection to the database.
    private static boolean open_connection() {

        // If a connection to the database has not yet been opened, open it.
        if (conn == null) {
            try {
                conn = source.getConnection();
            }
            catch (SQLException e) {

                // Return false if the connection was not successfully opened.
                System.out.println(e.getMessage());
                return false;
            }
        }

        // Return true if the connection was successfully opened.
        return true;
    }

    // Private helper function to close the connection to the database.
    private static boolean close_connection() {

        // If a connection to the database is currently open, close it.
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            }
            catch (SQLException e) {

                // Return false if the connection was not successfully closed.
                System.out.println(e.getMessage());
                return false;
            }
        }

        // Return true if the connection was successfully closed.
        return true;
    }

}