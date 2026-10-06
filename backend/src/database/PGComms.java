package database;

import java.sql.*;
import javax.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.postgresql.ds.PGSimpleDataSource;
import dto.InvEdibleRowDTO;
import dto.InvNonEdibleRowDTO;
import dto.EmployeesRowDTO;
import dto.MenuDrinksRowDTO;
import dto.MenuToppingsRowDTO;
import dto.JoinMenuDrinksAndInvEdibleRowDTO;
import dto.JoinMenuToppingsAndInvEdibleRowDTO;
import dto.OrdersRowDTO;

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

     /**
     * THIS FUNCTION DOES NOT SANITIZE QUERIES!<br>
     * Issues the query specified as a parameter to the database.
     * Queries here are "updates." These queries *change* data within the database.
     * A boolean is returned which indicates the success status of the query.<br>
     * Update Queries include: INSERT, UPDATE, DELETE.
     * @param query The "update" query (String) to be sent to the database.
     * @return true if the update query was successfully processed. false if it was not.
     */
    public static boolean issue_query_type_update(String query) {
        boolean query_success = false;
        try {
            open_connection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            query_success = true;
            ps.close();
        }
        catch (SQLException e) {

            // Return false if the query was not successfully issued.
            System.out.println(e.getMessage());
        }
        finally {

            // Always ensure the connection is closed.
            close_connection();
        }

        // Return query success/failure status.
        return query_success;
    }

    /**
     * THIS FUNCTION DOES NOT SANITIZE QUERIES!<br>
     * Issues the query specified as a parameter to the database.
     * Queries here are ones which return results. These queries *fetch* data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the inv_edible table.
     * @param query The query (String) to be sent to the database.
     * @return An array list of InvEdibleRowDTOs, where each element corresponds to an entire row from inv_edible.
     */
    public static ArrayList<InvEdibleRowDTO> issue_query_type_rows_InvEdible(String query) {
        ArrayList<InvEdibleRowDTO> items = new ArrayList<>();
        try {
            open_connection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new InvEdibleRowDTO(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
            ps.close();
        }
        catch (SQLException e) {

            // Return false if the query was not successfully issued.
            System.out.println(e.getMessage());
        }
        finally {

            // Always ensure the connection is closed.
            close_connection();
        }

        // Return the constructed ArrayList.
        return items;
    }

    /**
     * THIS FUNCTION DOES NOT SANITIZE QUERIES!<br>
     * Issues the query specified as a parameter to the database.
     * Queries here are ones which return results. These queries *fetch* data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the inv_nonedible table.
     * @param query The query (String) to be sent to the database.
     * @return An array list of InvNonEdibleRowDTOs, where each element corresponds to an entire row from inv_nonedible.
     */
    public static ArrayList<InvNonEdibleRowDTO> issue_query_type_rows_InvNonEdible(String query) {
        ArrayList<InvNonEdibleRowDTO> items = new ArrayList<>();
        try {
            open_connection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new InvNonEdibleRowDTO(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
            ps.close();
        }
        catch (SQLException e) {

            // Return false if the query was not successfully issued.
            System.out.println(e.getMessage());
        }
        finally {

            // Always ensure the connection is closed.
            close_connection();
        }

        // Return the constructed ArrayList.
        return items;
    }

    /**
     * THIS FUNCTION DOES NOT SANITIZE QUERIES!<br>
     * Issues the query specified as a parameter to the database.
     * Queries here are ones which return results. These queries *fetch* data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the employees table.
     * @param query The query (String) to be sent to the database.
     * @return An array list of EmployeesRowDTOs, where each element corresponds to an entire row from employees.
     */
    public static ArrayList<EmployeesRowDTO> issue_query_type_rows_Employees(String query) {
        ArrayList<EmployeesRowDTO> items = new ArrayList<>();
        try {
            open_connection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new EmployeesRowDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBigDecimal(5), rs.getInt(6)));
            }
            ps.close();
        }
        catch (SQLException e) {

            // Return false if the query was not successfully issued.
            System.out.println(e.getMessage());
        }
        finally {

            // Always ensure the connection is closed.
            close_connection();
        }

        // Return the constructed ArrayList.
        return items;
    }

    /**
     * THIS FUNCTION DOES NOT SANITIZE QUERIES!<br>
     * Issues the query specified as a parameter to the database.
     * Queries here are ones which return results. These queries *fetch* data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the menu_drinks table.
     * @param query The query (String) to be sent to the database.
     * @return An array list of MenuDrinksRowDTOs, where each element corresponds to an entire row from menu_drinks.
     */
    public static ArrayList<MenuDrinksRowDTO> issue_query_type_rows_MenuDrinks(String query) {
        ArrayList<MenuDrinksRowDTO> items = new ArrayList<>();
        try {
            open_connection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new MenuDrinksRowDTO(rs.getInt(1), rs.getString(2), rs.getBigDecimal(3), rs.getString(4), rs.getBoolean(5), rs.getBoolean(6)));
            }
            ps.close();
        }
        catch (SQLException e) {

            // Return false if the query was not successfully issued.
            System.out.println(e.getMessage());
        }
        finally {

            // Always ensure the connection is closed.
            close_connection();
        }

        // Return the constructed ArrayList.
        return items;
    }

    /**
     * THIS FUNCTION DOES NOT SANITIZE QUERIES!<br>
     * Issues the query specified as a parameter to the database.
     * Queries here are ones which return results. These queries *fetch* data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the menu_toppings table.
     * @param query The query (String) to be sent to the database.
     * @return An array list of MenuToppingsRowDTOs, where each element corresponds to an entire row from menu_toppings.
     */
    public static ArrayList<MenuToppingsRowDTO> issue_query_type_rows_MenuToppings(String query) {
        ArrayList<MenuToppingsRowDTO> items = new ArrayList<>();
        try {
            open_connection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new MenuToppingsRowDTO(rs.getInt(1), rs.getString(2), rs.getBigDecimal(3)));
            }
            ps.close();
        }
        catch (SQLException e) {

            // Return false if the query was not successfully issued.
            System.out.println(e.getMessage());
        }
        finally {

            // Always ensure the connection is closed.
            close_connection();
        }

        // Return the constructed ArrayList.
        return items;
    }

    /**
     * THIS FUNCTION DOES NOT SANITIZE QUERIES!<br>
     * Issues the query specified as a parameter to the database.
     * Queries here are ones which return results. These queries *fetch* data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the join_menu_drinks_and_inv_edible table.
     * @param query The query (String) to be sent to the database.
     * @return An array list of JoinMenuDrinksAndInvEdibleRowDTOs, where each element corresponds to an entire row from join_menu_drinks_and_inv_edible.
     */
    public static ArrayList<JoinMenuDrinksAndInvEdibleRowDTO> issue_query_type_rows_JoinMenuDrinksAndInvEdible(String query) {
        ArrayList<JoinMenuDrinksAndInvEdibleRowDTO> items = new ArrayList<>();
        try {
            open_connection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new JoinMenuDrinksAndInvEdibleRowDTO(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
            }
            ps.close();
        }
        catch (SQLException e) {

            // Return false if the query was not successfully issued.
            System.out.println(e.getMessage());
        }
        finally {

            // Always ensure the connection is closed.
            close_connection();
        }

        // Return the constructed ArrayList.
        return items;
    }

    /**
     * THIS FUNCTION DOES NOT SANITIZE QUERIES!<br>
     * Issues the query specified as a parameter to the database.
     * Queries here are ones which return results. These queries *fetch* data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the join_menu_toppings_and_inv_edible table.
     * @param query The query (String) to be sent to the database.
     * @return An array list of JoinMenuToppingsAndInvEdibleRowDTOs, where each element corresponds to an entire row from join_menu_toppings_and_inv_edible.
     */
    public static ArrayList<JoinMenuToppingsAndInvEdibleRowDTO> issue_query_type_rows_JoinMenuToppingsAndInvEdible(String query) {
        ArrayList<JoinMenuToppingsAndInvEdibleRowDTO> items = new ArrayList<>();
        try {
            open_connection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new JoinMenuToppingsAndInvEdibleRowDTO(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
            }
            ps.close();
        }
        catch (SQLException e) {

            // Return false if the query was not successfully issued.
            System.out.println(e.getMessage());
        }
        finally {

            // Always ensure the connection is closed.
            close_connection();
        }

        // Return the constructed ArrayList.
        return items;
    }

    /**
     * THIS FUNCTION DOES NOT SANITIZE QUERIES!<br>
     * Issues the query specified as a parameter to the database.
     * Queries here are ones which return results. These queries *fetch* data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the orders table.
     * @param query The query (String) to be sent to the database.
     * @return An array list of OrdersRowDTOs, where each element corresponds to an entire row from orders.
     */
    public static ArrayList<OrdersRowDTO> issue_query_type_rows_Orders(String query) {
        ArrayList<OrdersRowDTO> items = new ArrayList<>();
        try {
            open_connection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new OrdersRowDTO(rs.getInt(1), rs.getBoolean(2), rs.getObject(3, OffsetDateTime.class), rs.getObject(4, OffsetDateTime.class), rs.getBigDecimal(5), rs.getInt(6), rs.getBigDecimal(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getInt(11), rs.getInt(12), rs.getBoolean(13)));
            }
            ps.close();
        }
        catch (SQLException e) {

            // Return false if the query was not successfully issued.
            System.out.println(e.getMessage());
        }
        finally {

            // Always ensure the connection is closed.
            close_connection();
        }

        // Return the constructed ArrayList.
        return items;
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