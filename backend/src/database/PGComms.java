package database;

import java.sql.*;
import javax.sql.*;
import org.postgresql.ds.PGSimpleDataSource;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

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

    // Test if the database can be opened and closed before it is regularly used.
    // If it cannot be opened, return false. If it can be opened, return true.
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

    // Return the names of all drinks in menu_drinks whose "type" column equals the given type,
    // ordered by id_drink. Returns an empty list if the database can't be reached or the query fails.
    public static List<String> get_drink_names_by_type(String type) {
        List<String> names = new ArrayList<>();
        if (!open_connection()) {
            return names;
        }

        String sql = "SELECT name FROM menu_drinks WHERE type = ? ORDER BY id_drink";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    names.add(rs.getString("name"));
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        close_connection();
        return names;
    }

    // Return the name of every topping in menu_toppings, ordered by id_topping.
    public static List<String> get_topping_names() {
        List<String> names = new ArrayList<>();

        if (!open_connection()) {
            return names;
        }

        String sql = "SELECT name FROM menu_toppings ORDER BY id_topping";
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        close_connection();
        return names;
    }

    // Return true if the named drink has hot_available = true in menu_drinks.
    public static boolean is_hot_available(String drink_name) {
        boolean hot = false;

        if (!open_connection()) {
            return hot;
        }

        String sql = "SELECT hot_available FROM menu_drinks WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, drink_name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    hot = rs.getBoolean("hot_available");
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        close_connection();
        return hot;
    }

    private static BigDecimal get_price(String sql, String name) {
        BigDecimal price = BigDecimal.ZERO;

        if (!open_connection()) {
            return price;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal p = rs.getBigDecimal("price");
                    if (p != null) price = p;
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        close_connection();
        return price;
    }
    
    public static BigDecimal get_drink_price(String name) {
        return get_price("SELECT price FROM menu_drinks WHERE name = ?", name);
    }

    public static BigDecimal get_topping_price(String name) {
        return get_price("SELECT price FROM menu_toppings WHERE name = ?", name);
    }
}