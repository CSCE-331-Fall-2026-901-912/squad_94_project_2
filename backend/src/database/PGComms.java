package database;

import java.sql.*;
import javax.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import org.postgresql.ds.PGSimpleDataSource;
import dto.InvEdibleRowDTO;
import dto.InvNonEdibleRowDTO;
import dto.EmployeesRowDTO;
import dto.MenuDrinksRowDTO;
import dto.MenuToppingsRowDTO;
import dto.JoinMenuDrinksAndInvEdibleRowDTO;
import dto.JoinMenuToppingsAndInvEdibleRowDTO;
import dto.OrdersRowDTO;
import model.Employee;

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

    public static boolean add_employee(String name, String position, String phone,
                                   BigDecimal pay_rate, int hours) {
        if (!open_connection()) {
            return false;
        }

        String sql = "INSERT INTO employees "
                + "(id_employee, name, position, phone_number, current_pay_rate, hours_worked_for_week) "
                + "VALUES ((SELECT COALESCE(MAX(id_employee), 0) + 1 FROM employees), ?, ?, ?, ?, ?)";
        boolean saved = false;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, position);
            stmt.setString(3, phone);
            stmt.setBigDecimal(4, pay_rate);
            stmt.setInt(5, hours);
            saved = stmt.executeUpdate() == 1;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        close_connection();
        return saved;
    }

    public static boolean update_employee(int id, String name, String position, String phone,
                                      BigDecimal pay, int hours) {
        if (!open_connection()) {
            return false;
        }

        String sql = "UPDATE employees SET name = ?, position = ?, phone_number = ?, "
                + "current_pay_rate = ?, hours_worked_for_week = ? WHERE id_employee = ?";
        boolean ok = false;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, position);
            stmt.setString(3, phone);
            stmt.setBigDecimal(4, pay);
            stmt.setInt(5, hours);
            stmt.setInt(6, id);
            ok = stmt.executeUpdate() == 1;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        close_connection();
        return ok;
    }

    public static boolean add_drink_with_ingredients(
            String name, BigDecimal price, String type,
            boolean hot_available, boolean is_non_caffeinated,
            List<Integer> edible_ids) {
        if (!open_connection()) {
            return false;
        }

        boolean saved = false;
        try {
            conn.setAutoCommit(false);

            int drink_id;
            String drink_sql = "INSERT INTO menu_drinks "
                    + "(id_drink, name, price, type, hot_available, is_non_caffeinated) "
                    + "VALUES ((SELECT COALESCE(MAX(id_drink), 0) + 1 FROM menu_drinks), ?, ?, ?, ?, ?) "
                    + "RETURNING id_drink";
            try (PreparedStatement drink_stmt = conn.prepareStatement(drink_sql)) {
                drink_stmt.setString(1, name);
                drink_stmt.setBigDecimal(2, price);
                drink_stmt.setString(3, type);
                drink_stmt.setBoolean(4, hot_available);
                drink_stmt.setBoolean(5, is_non_caffeinated);

                try (ResultSet result = drink_stmt.executeQuery()) {
                    if (!result.next()) {
                        conn.rollback();
                        return false;
                    }
                    drink_id = result.getInt("id_drink");
                }
            }

            String ingredient_sql = "INSERT INTO join_menu_drinks_and_inv_edible "
                    + "(id_join_menu_drinks_and_inv_edible, id_drink, id_edible) "
                    + "VALUES ((SELECT COALESCE(MAX(id_join_menu_drinks_and_inv_edible), 0) + 1 "
                    + "FROM join_menu_drinks_and_inv_edible), ?, ?)";
            try (PreparedStatement ingredient_stmt = conn.prepareStatement(ingredient_sql)) {
                for (int edible_id : edible_ids) {
                    ingredient_stmt.setInt(1, drink_id);
                    ingredient_stmt.setInt(2, edible_id);
                    ingredient_stmt.addBatch();
                }
                ingredient_stmt.executeBatch();
            }

            conn.commit();
            saved = true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollback_error) {
                System.out.println(rollback_error.getMessage());
            }
            System.out.println(e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            close_connection();
        }

        return saved;
    }

    public static boolean update_drink_with_ingredients(
            int drink_id, String name, BigDecimal price, String type,
            boolean hot_available, boolean is_non_caffeinated,
            List<Integer> edible_ids) {
        if (!open_connection()) {
            return false;
        }

        boolean saved = false;
        try {
            conn.setAutoCommit(false);

            String drink_sql = "UPDATE menu_drinks SET name = ?, price = ?, type = ?, "
                    + "hot_available = ?, is_non_caffeinated = ? WHERE id_drink = ?";
            try (PreparedStatement drink_stmt = conn.prepareStatement(drink_sql)) {
                drink_stmt.setString(1, name);
                drink_stmt.setBigDecimal(2, price);
                drink_stmt.setString(3, type);
                drink_stmt.setBoolean(4, hot_available);
                drink_stmt.setBoolean(5, is_non_caffeinated);
                drink_stmt.setInt(6, drink_id);
                if (drink_stmt.executeUpdate() != 1) {
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement delete_stmt = conn.prepareStatement(
                    "DELETE FROM join_menu_drinks_and_inv_edible WHERE id_drink = ?")) {
                delete_stmt.setInt(1, drink_id);
                delete_stmt.executeUpdate();
            }

            String ingredient_sql = "INSERT INTO join_menu_drinks_and_inv_edible "
                    + "(id_join_menu_drinks_and_inv_edible, id_drink, id_edible) "
                    + "VALUES ((SELECT COALESCE(MAX(id_join_menu_drinks_and_inv_edible), 0) + 1 "
                    + "FROM join_menu_drinks_and_inv_edible), ?, ?)";
            try (PreparedStatement ingredient_stmt = conn.prepareStatement(ingredient_sql)) {
                for (int edible_id : edible_ids) {
                    ingredient_stmt.setInt(1, drink_id);
                    ingredient_stmt.setInt(2, edible_id);
                    ingredient_stmt.addBatch();
                }
                ingredient_stmt.executeBatch();
            }

            conn.commit();
            saved = true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollback_error) {
                System.out.println(rollback_error.getMessage());
            }
            System.out.println(e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            close_connection();
        }

        return saved;
    }

    public static boolean add_topping_with_inventory(String name, BigDecimal price) {
        if (!open_connection()) {
            return false;
        }

        try {
            conn.setAutoCommit(false);
            int topping_id;
            int edible_id;

            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO menu_toppings (id_topping, name, price) "
                    + "VALUES ((SELECT COALESCE(MAX(id_topping), 0) + 1 FROM menu_toppings), ?, ?) "
                    + "RETURNING id_topping")) {
                stmt.setString(1, name);
                stmt.setBigDecimal(2, price);
                try (ResultSet result = stmt.executeQuery()) {
                    if (!result.next()) {
                        conn.rollback();
                        return false;
                    }
                    topping_id = result.getInt("id_topping");
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO inv_edible (id_edible, name, amount_servings) "
                    + "VALUES ((SELECT COALESCE(MAX(id_edible), 0) + 1 FROM inv_edible), ?, 400) "
                    + "RETURNING id_edible")) {
                stmt.setString(1, name);
                try (ResultSet result = stmt.executeQuery()) {
                    if (!result.next()) {
                        conn.rollback();
                        return false;
                    }
                    edible_id = result.getInt("id_edible");
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO join_menu_toppings_and_inv_edible "
                    + "(id_join_menu_toppings_and_inv_edible, id_topping, id_edible) "
                    + "VALUES ((SELECT COALESCE(MAX(id_join_menu_toppings_and_inv_edible), 0) + 1 "
                    + "FROM join_menu_toppings_and_inv_edible), ?, ?)")) {
                stmt.setInt(1, topping_id);
                stmt.setInt(2, edible_id);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            rollback_connection();
            System.out.println(e.getMessage());
            return false;
        } finally {
            reset_connection();
        }
    }

    public static boolean update_topping_with_inventory(
            int topping_id, String name, BigDecimal price) {
        if (!open_connection()) {
            return false;
        }

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE menu_toppings SET name = ?, price = ? WHERE id_topping = ?")) {
                stmt.setString(1, name);
                stmt.setBigDecimal(2, price);
                stmt.setInt(3, topping_id);
                if (stmt.executeUpdate() != 1) {
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE inv_edible SET name = ? WHERE id_edible = "
                    + "(SELECT id_edible FROM join_menu_toppings_and_inv_edible "
                    + "WHERE id_topping = ?)")) {
                stmt.setString(1, name);
                stmt.setInt(2, topping_id);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            rollback_connection();
            System.out.println(e.getMessage());
            return false;
        } finally {
            reset_connection();
        }
    }

    private static void rollback_connection() {
        try {
            conn.rollback();
        } catch (SQLException rollback_error) {
            System.out.println(rollback_error.getMessage());
        }
    }

    private static void reset_connection() {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException reset_error) {
            System.out.println(reset_error.getMessage());
        }
        close_connection();
    }
}