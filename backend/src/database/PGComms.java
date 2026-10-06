package database;

import java.sql.*;
import javax.sql.*;
import org.postgresql.ds.PGSimpleDataSource;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public static void insert_order_item(int order_id, double order_total, double tip, int id_drink, int id_topping1, int id_topping2, int ice_level, int sugar_level, boolean hot_chosen) {
        if (!open_connection()) {
            return;
        }
        // int:1, boolean, timestamptz, timestamptz, numeric, int:employee_id, numeric, int:id_drink, int:id_topping1, int:id_topping2, int:ice_level (0,1,2), int:sugar_level (0,1,2,3,4), boolean:hot_chosen
        String sql = "INSERT INTO order_items (id_order, completed, time_created_at, time_completed_at, total_spent, id_employee, tip, id_drink, id_topping1, id_topping2, ice_level, sugar_level, hot_chosen) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            BigDecimal price;
            BigDecimal tip_amount;
            price = BigDecimal.valueOf(order_total);
            tip_amount = BigDecimal.valueOf(tip);

            stmt.setInt(1, order_id);
            stmt.setBoolean(2, false);
            stmt.setTimestamp(3, null); //created time
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); // time completed
            stmt.setBigDecimal(5, price);
            // stmt.setInt(6, id_employee); // GET CURRENT EMPLOYEE ID
            stmt.setBigDecimal(7, tip_amount);
            stmt.setInt(8, id_drink);
            stmt.setInt(9, id_topping1);
            stmt.setInt(10, id_topping2);
            stmt.setInt(8, ice_level);
            stmt.setInt(9, sugar_level);
            stmt.setBoolean(10, hot_chosen);

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        close_connection();
    }
}