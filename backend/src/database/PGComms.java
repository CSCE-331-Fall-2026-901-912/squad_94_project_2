package database;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import dto.*;
import org.postgresql.ds.PGSimpleDataSource;

/**
 * Manages all application related to the PostgreSQL JDBC driver.
 * Encapsulates all communication to the application-specific database.
 * Manages opening and closing of connection, reads to the database and writes to the database.
 * <br><br>
 * PGComms is not designed to broadly work with all PostgreSQL databases, but rather specifically this application's database.
 * @author Squad 94 - Rigo Chiti, Sanjana Ram, Asher Blevins, Natalie Gonzalez and Mairead Finnerty
 */
public class PGComms {

    /**
     * A simple non-pooling database manager which provides a connection to the database.
     * Initialized with a large number of fields which describe the database and its properties.
     *
     * @see #initialize_database(String server_name, String database_name, String user, String password)
     */
    // Define the unique database source this class allows one to refer to.
    private static PGSimpleDataSource source = null;

    /**
     * Abstracted field used to reference the connection to the database.
     * {@link #source} is used to retrieve a connection, which is then stored in this field.
     * When the connection is not open, the field contains {@code null}.
     *
     * @see #source
     */
    // Define a variable to hold the database connection.
    private static Connection conn = null;

    /**
     * Set all required attributes of the database necessary open and close connections to it.
     * Attributes are those of {@link #source}.
     *
     * @param server_name The name of the server hosting the database, likely an IP or similar alias.
     * @param database_name The name of the database that PGComms should enable one to connect to.
     * @param user The name of the user PGComms will manage the database connection for.
     * @param password The password to the database.
     *
     * @see #source
     */
    public static void initialize_database(String server_name, String database_name, String user, String password) {
        source = new PGSimpleDataSource();
        source.setServerNames(new String[]{server_name});
        source.setDatabaseName(database_name);
        source.setUser(user);
        source.setPassword(password);
    }

    /**
     * Indicate if a successful connection was opened, then closed, to the stored database.
     *
     * @return {@code true} if the connection was opened, then closed. {@code false} if the connection could not be opened and/or closed.
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
     * <b>Private helper function.</b><br>
     * Opens a connection to the database using the information stored in {@link #source}.
     * The connection is stored in {@link #conn}. While the connection is open, database
     * communication is managed using {@link #conn}.
     *
     * @return {@code true} if the connection was open. {@code false} if the connection could not be opened.
     */
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

    /**
     * <b>Private helper function.</b><br>
     * Closes the connection to the database stored in {@link #conn}.
     * If {@link #conn} holds {@code null}, there is no open connection, and so no connection will be closed.
     * Once the connection is successfully closed, {@link #conn} is set to {@code null}.
     *
     * @return {@code true} if the connection was closed. {@code false} if the connection could not be closed.
     */
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
     * <b>THIS FUNCTION DOES NOT SANITIZE QUERIES!</b><br>
     * Issues the query specified as a parameter to the database.
     * Queries here are "updates." These queries <b>change</b> data within the database.
     * A boolean is returned which indicates the success status of the query.<br>
     * <i>Update</i> Queries include: {@code INSERT}, {@code UPDATE}, {@code DELETE}.
     *
     * @param query The <i>update</i> query to be sent to the database.
     * @return {@code true} if the <i>update</i> query was successfully processed. {@code false} if it was not.
     */
    public static boolean issue_query_type_update(String query) {
        boolean query_success = false;
        try {
            if (!open_connection()) {
                return false;
            }
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.executeUpdate();
                query_success = true;
            }
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
     * <b>THIS FUNCTION DOES NOT SANITIZE QUERIES!</b><br>
     * Issues the query specified as a parameter to the database concerning the {@code inv_edible} table.
     * Queries here are ones which return results. These queries <b>fetch</b> data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the {@code inv_edible} table.
     *
     * @param query The query to be sent to the database.
     * @return An {@link ArrayList} of {@link InvEdibleRowDTO}s, where each element corresponds to an entire row from {@code inv_edible}.
     *
     * @see InvEdibleRowDTO
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
     * <b>THIS FUNCTION DOES NOT SANITIZE QUERIES!</b><br>
     * Issues the query specified as a parameter to the database concerning the {@code inv_nonedible} table.
     * Queries here are ones which return results. These queries <b>fetch</b> data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the {@code inv_nonedible} table.
     *
     * @param query The query to be sent to the database.
     * @return An {@link ArrayList} of {@link InvNonEdibleRowDTO}s, where each element corresponds to an entire row from {@code inv_nonedible}.
     *
     * @see InvNonEdibleRowDTO
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
     * <b>THIS FUNCTION DOES NOT SANITIZE QUERIES!</b><br>
     * Issues the query specified as a parameter to the database concerning the {@code employees} table.
     * Queries here are ones which return results. These queries <b>fetch</b> data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the {@code employees} table.
     *
     * @param query The query to be sent to the database.
     * @return An {@link ArrayList} of {@link EmployeesRowDTO}s, where each element corresponds to an entire row from {@code employees}.
     *
     * @see EmployeesRowDTO
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
     * <b>THIS FUNCTION DOES NOT SANITIZE QUERIES!</b><br>
     * Issues the query specified as a parameter to the database concerning the {@code menu_drinks} table.
     * Queries here are ones which return results. These queries <b>fetch</b> data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the {@code menu_drinks} table.
     *
     * @param query The query to be sent to the database.
     * @return An {@link ArrayList} of {@link MenuDrinksRowDTO}s, where each element corresponds to an entire row from {@code menu_drinks}.
     *
     * @see MenuDrinksRowDTO
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
     * <b>THIS FUNCTION DOES NOT SANITIZE QUERIES!</b><br>
     * Issues the query specified as a parameter to the database concerning the {@code menu_toppings} table.
     * Queries here are ones which return results. These queries <b>fetch</b> data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the {@code menu_toppings} table.
     *
     * @param query The query to be sent to the database.
     * @return An {@link ArrayList} of {@link MenuToppingsRowDTO}s, where each element corresponds to an entire row from {@code menu_toppings}.
     *
     * @see MenuToppingsRowDTO
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
     * <b>THIS FUNCTION DOES NOT SANITIZE QUERIES!</b><br>
     * Issues the query specified as a parameter to the database concerning the {@code join_menu_drinks_and_inv_edible} table.
     * Queries here are ones which return results. These queries <b>fetch</b> data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the {@code join_menu_drinks_and_inv_edible} table.
     *
     * @param query The query to be sent to the database.
     * @return An {@link ArrayList} of {@link JoinMenuDrinksAndInvEdibleRowDTO}s, where each element corresponds to an entire row from {@code join_menu_drinks_and_inv_edible}.
     *
     * @see JoinMenuDrinksAndInvEdibleRowDTO
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
     * <b>THIS FUNCTION DOES NOT SANITIZE QUERIES!</b><br>
     * Issues the query specified as a parameter to the database concerning the {@code join_menu_toppings_and_inv_edible} table.
     * Queries here are ones which return results. These queries <b>fetch</b> data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the {@code join_menu_toppings_and_inv_edible} table.
     *
     * @param query The query to be sent to the database.
     * @return An {@link ArrayList} of {@link JoinMenuToppingsAndInvEdibleRowDTO}s, where each element corresponds to an entire row from {@code join_menu_toppings_and_inv_edible}.
     *
     * @see JoinMenuToppingsAndInvEdibleRowDTO
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
     * <b>THIS FUNCTION DOES NOT SANITIZE QUERIES!</b><br>
     * Issues the query specified as a parameter to the database concerning the {@code orders} table.
     * Queries here are ones which return results. These queries <b>fetch</b> data within the database.
     * This function assumes that the resulting query will see entire row(s) returned from the {@code orders} table.
     *
     * @param query The query to be sent to the database.
     * @return An {@link ArrayList} of {@link OrdersRowDTO}s, where each element corresponds to an entire row from {@code orders}.
     *
     * @see OrdersRowDTO
     */
    public static ArrayList<OrdersRowDTO> issue_query_type_rows_Orders(String query) {
        ArrayList<OrdersRowDTO> items = new ArrayList<>();
        try {
            open_connection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new OrdersRowDTO(rs.getInt(1), rs.getString(2), rs.getObject(3, OffsetDateTime.class), rs.getBigDecimal(4), rs.getInt(5), rs.getBigDecimal(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getInt(11), rs.getBoolean(12)));
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

    // A lot of these SQL queries were created before the generic query functions were implemented, so they are not using the generic functions
    // They may eventually be refactored to use the generic functions at a later date

    /**
     * Return the names of all drinks in the {@code menu_drinks} table with the specified type.
     * If multiple drink names are returned, they are ordered by their internal {@code id_drink} stored in {@code menu_drinks}.
     *
     * @param type The specified (drink) type.
     * @return A list of drink names ordered by their {@code id_drink} in the {@code menu_drinks} table.
     */
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

    /**
     * Return the names of all drinks with the specified type in the {@code menu_drinks} table who are sold out.
     * Sold out meaning all drinks who depend upon items which are currently out of stock in {@code inv_edible} or {@code inv_nonedible}.
     *
     * @param type The specified (drink) type.
     * @return A list of drink names ordered by their {@code id_drink} in the {@code menu_drinks}.
     */
    public static List<String> get_sold_out_drink_names_by_type(String type) {
        List<String> names = new ArrayList<>();
        if (!open_connection()) {
            return names;
        }

        String sql = "SELECT d.name FROM menu_drinks d "
                + "WHERE d.type = ? AND EXISTS ("
                + "SELECT 1 FROM join_menu_drinks_and_inv_edible j "
                + "JOIN inv_edible i ON i.id_edible = j.id_edible "
                + "WHERE j.id_drink = d.id_drink AND i.amount_servings <= 0"
                + ") ORDER BY d.id_drink";
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

    /**
     * Return all items from the {@code inv_edible} and {@code inv_nonedible} tables which are currently out-of-stock.
     * Out-of-stock meaning there are 0 of the item in either the {@code inv_edible} and {@code inv_nonedible} tables.
     *
     * @return A {@link List} of {@link IDNameValDTO}s, where each element corresponds to an item from either {@code inv_edible} or {@code inv_nonedible}.
     *
     * @see IDNameValDTO
     */
    // Get all items from the edible and nonedible inventories which have an amount of 0.
    public static List<IDNameValDTO> get_out_of_stock() {
        List<InvNonEdibleRowDTO> inv_non_edible_select = issue_query_type_rows_InvNonEdible("SELECT * FROM inv_nonedible WHERE amount = 0");
        List<InvEdibleRowDTO> inv_edible_select = issue_query_type_rows_InvEdible("SELECT * FROM inv_edible WHERE amount_servings = 0");
        return get_inv_selection_common(inv_non_edible_select, inv_edible_select);
    }

    /**
     * Return all items from the {@code inv_edible} and {@code inv_nonedible} tables which are currently running low.<br>
     * Running low for edible items meaning <b>there are more than 0, but less than or equal to 500 servings</b> left in the {@code inv_edible} table.<br>
     * Running low for non-edible items meaning <b>there are more than 0, but less than or equal to 500</b> of the item left in the {@code inv_nonedible} table.
     *
     * @return A {@link List} of {@link IDNameValDTO}s, where each element corresponds to an item from either {@code inv_edible} or {@code inv_nonedible}.
     *
     * @see IDNameValDTO
     */
    // Get all items from the edible and nonedible inventories which are close to being, but are not currently, out-of-stock.
    public static List<IDNameValDTO> get_running_low_on() {
        List<InvNonEdibleRowDTO> inv_non_edible_select = issue_query_type_rows_InvNonEdible("SELECT * FROM inv_nonedible WHERE amount > 0 AND amount <= 500 ORDER BY amount");
        List<InvEdibleRowDTO> inv_edible_select = issue_query_type_rows_InvEdible("SELECT * FROM inv_edible WHERE amount_servings > 0 AND amount_servings <= 500 ORDER BY amount_servings");
        return get_inv_selection_common(inv_non_edible_select, inv_edible_select);
    }

    /**
     * Report if all <b>required</b> non-edible items have an amount strictly greater than the specified amount in the {@code inv_nonedible} table.<br>
     * Required non-edible items are: {@code Lids}, {@code Cups}, {@code Straws}, {@code Lid Covers}.
     *
     * @param required_quantity The quantity all <b>required</b> non-edible items must have an inventory amount strictly greater than.
     * @return {@code true} if all <b>required</b> non-edible inventory items have an amount above the specified quantity. Return {@code false} if this is not the case or if one of the items is not in the table.
     */
    // Return true only when all required non-edible inventory items have enough stock.
    public static boolean are_required_nonedible_items_available(int required_quantity) {
        if (required_quantity < 1) {
            throw new IllegalArgumentException("Required quantity must be positive.");
        }

        List<InvNonEdibleRowDTO> required_items =
                issue_query_type_rows_InvNonEdible(
                        "SELECT * FROM inv_nonedible "
                        + "WHERE id_nonedible IN (1, 2, 3, 4)");

        if (required_items.size() != 4) {
            return false;
        }

        for (InvNonEdibleRowDTO item : required_items) {
            if (item.amount() < required_quantity) {
                return false;
            }
        }
        return true;
    }

    /**
     * <b>Private helper function.</b><br>
     * Returns a common {@code List} containing elements derived from the {@code inv_edible} and {@code inv_nonedible} lists.
     * Allowing the normally-separate elements from each table to be placed into one {@code List}.
     *
     * @param inv_non_edible_select {@code List} of {@link InvNonEdibleRowDTO}s, where each element corresponds to an item from {@code inv_nonedible}.
     * @param inv_edible_select {@code List} of {@link InvEdibleRowDTO}s, where each element corresponds to an item from {@code inv_edible}.
     * @return An {@link ArrayList} of {@link IDNameValDTO}s, where each element corresponds to an item from either {@code inv_edible} or {@code inv_nonedible}.
     *
     * @see InvNonEdibleRowDTO
     * @see InvEdibleRowDTO
     * @see IDNameValDTO
     */
    // Shared private helper function by get_out_of_stock() and get_running_low_on() to return items from both inv_edible and inv_nonedible row-by-row in one list.
    // Each public function differs in the conditions it uses in its SQL queries.
    private static List<IDNameValDTO> get_inv_selection_common(List<InvNonEdibleRowDTO> inv_non_edible_select, List<InvEdibleRowDTO> inv_edible_select) {
        List<IDNameValDTO> output = new ArrayList<>();
        for (InvNonEdibleRowDTO item : inv_non_edible_select) {
            output.add(new IDNameValDTO(item.id_nonedible(), item.name(), item.amount()));
        }

        for (InvEdibleRowDTO item : inv_edible_select) {
            output.add(new IDNameValDTO(item.id_edible(), item.name(), item.amount_servings()));
        }

        return output;
    }

    /**
     * Get all {@code id_edible} values for items in the {@code inv_edible} table which must be consumed for the drink which has the specified ID.
     *
     * @param id_drink The specified ID of the drink in the {@code menu_drinks} table.
     * @return A list of all {@code id_edible}s for all items in {@code inv_edible} which must be consumed to make the specified drink.
     */
    // Get all id_edible values that correspond to edible inventory that must be consumed for a specific drink (based on id_drink).
    public static ArrayList<Integer> get_id_edibles_for_drink(int id_drink) {
        ArrayList<Integer> id_edibles = new ArrayList<>();

        if (!open_connection()) {
            return id_edibles;
        }

        String sql = "SELECT id_edible FROM join_menu_drinks_and_inv_edible WHERE id_drink = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_drink);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    id_edibles.add(rs.getInt("id_edible"));
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        close_connection();
        return id_edibles;
    }

    /**
     * Return the names of every topping in the {@code menu_toppings} table.
     * If multiple topping names are returned, they are ordered by their internal {@code id_topping} stored in {@code menu_toppings}.
     *
     * @return A list of topping names ordered by their {@code id_topping} in the {@code menu_toppings} table.
     */
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

    /**
     * Reports if the drink with the specified name can be served hot.
     *
     * @param drink_name The name of the drink.
     * @return {@code true} if the drink can be served hot. {@code false} if the drink cannot be served hot.
     */
    // Return true if the named drink has hot_available = true in menu_drinks
    // Used to determine if the hot button should be enabled for a drink in the order window
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

    /**
     * <b>Private helper function.</b><br>
     * Retrieves the price of a menu item, either in {@code menu_drinks} or {@code menu_toppings} corresponding to the specified name.
     *
     * @param sql An SQL query, which should be structured to either retrieve a row's price from {@code menu_drinks} or {@code menu_toppings} where the row's {@code name} matches the specified name.
     * @param name The name of either the drink in {@code menu_drinks} or {@code menu_toppings}.
     * @return The price of the drink/topping.
     */
    // Retrieves the price of a menu item (drink or topping) from the database based on its name.
    // Used to calculate totals for order submission and to display the price in the order summary.
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

    /**
     * Retrieves the price of a drink in {@code menu_drinks}.
     *
     * @param name The name of the drink.
     * @return The price of the drink.
     */
    public static BigDecimal get_drink_price(String name) {
        return get_price("SELECT price FROM menu_drinks WHERE name = ?", name);
    }

    /**
     * Retrieves the price of a topping in {@code menu_toppings}.
     *
     * @param name The name of the topping.
     * @return The price of the topping.
     */
    public static BigDecimal get_topping_price(String name) {
        return get_price("SELECT price FROM menu_toppings WHERE name = ?", name);
    }

    /**
     * Retrieves the total sales made from all orders with the status {@code "completed"} in the {@code orders} table.
     *
     * @return The all-time total sales made from completed (not voided or discarded) orders.
     */
    // Get the total sales from the orders table where status = 'completed'. 
    // Returns 0 if the database can't be reached or the query fails.
    // Used to calculate total sales in the reports window.
    public static BigDecimal get_sales_total() {
        BigDecimal price = BigDecimal.ZERO;

        String sql = "SELECT SUM(total_spent) AS sum_total FROM orders WHERE status = ?";

        if (!open_connection()) {
            return price;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "completed");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal p = rs.getBigDecimal("sum_total");
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

    /**
     * Retrieves the total sales made from all orders with the status {@code "completed"} on the specified date.
     *
     * @param date_input The specified date, a {@link String} formatted "YYYY-MM-DD." <b>Formatting must be followed.</b>
     * @return The all-time total sales made from completed (not voided or discarded) orders on the specified date.
     */
    // Get the total sales from the orders table where status = 'completed' and where the order happened on date_input.
    // Similar to get_sales_total(), but exists as a separate function due to the complexity of
    // restricting by a date range.
    // Though this function retrieves total price on a specific date, nuances of the LocalDate library have caused implementation
    // to perform an SQL query with a date range (beginning of specified date to end of specified date).
    public static BigDecimal get_sales_total_date(String date_input) {
        BigDecimal price = BigDecimal.ZERO;

        String sql = "SELECT SUM(total_spent) AS sum_total FROM orders WHERE status = ? AND time_created_at >= ? AND time_created_at < ?";

        if (!open_connection()) {
            return price;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDate day = LocalDate.parse(date_input);
            stmt.setString(1, "completed");
            stmt.setObject(2, day.atStartOfDay());
            stmt.setObject(3, day.plusDays(1).atStartOfDay());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal p = rs.getBigDecimal("sum_total");
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

    /**
     * For each hour of the specified date, reports the total number of orders with status {@code "completed"}, total number with status {@code "void"}, and total with status {@code "discard"}.
     * An hour that sees no orders completed, voided, nor discarded will not be reported. Times are reported assuming the time zone is {@code America/Chicago}.
     *
     * @param date_input The specified date, a {@link String} formatted "YYYY-MM-DD." <b>Formatting must be followed.</b>
     * @return A string with entries separated each by two new lines. Each entry is for one hour of the day--the date and time are reported, and total number of orders completed, voided, and ordered are listed.
     */
    // Get a count of completed, void, and discard orders per hour for a specified day.
    public static String get_xreport_string(String date_input) {
        String sql = "SELECT\n" +
                "    date_trunc('hour', time_created_at AT TIME ZONE 'America/Chicago') AS sales_hour,\n" +
                "    COUNT(*) FILTER (WHERE status = 'completed') AS completed,\n" +
                "\tCOUNT(*) FILTER (WHERE status = 'void') AS void,\n" +
                "\tCOUNT(*) FILTER (WHERE status = 'discard') AS discard\n" +
                "FROM orders\n" +
                "WHERE time_created_at >= ? AND time_created_at < ?\n" +
                "GROUP BY sales_hour\n" +
                "ORDER BY sales_hour;";

        String output = "";

        if (!open_connection()) {
            return output;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDate day = LocalDate.parse(date_input);
            ZoneId centralTime = ZoneId.of("America/Chicago");

            Instant start = day.atStartOfDay(centralTime).toInstant();
            Instant end = day.plusDays(1).atStartOfDay(centralTime).toInstant();

            stmt.setTimestamp(1, Timestamp.from(start));
            stmt.setTimestamp(2, Timestamp.from(end));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime time = rs.getObject(1, LocalDateTime.class);
                    String num_completed = rs.getString(2);
                    String num_void = rs.getString(3);
                    String num_discard = rs.getString(4);
                    output += "-- Within the hour beginning at " + time.toLocalDate() + " " + time.toLocalTime() + " --\n" + "Orders completed: " +
                    num_completed + "\n" + "Orders voided: " + num_void + "\n" + "Orders discarded: " + num_discard +
                    "\n\n";
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        close_connection();
        return output;
    }

    /**
     * <b>THIS FUNCTION DOES NOT SANITIZE QUERIES!</b><br>
     * Inserts a new row into the {@code orders} table with the specified attributes.
     * <br><b>If the query cannot be successfully issued, the console will print an error message.</b>
     *
     * @param order_id The ID of the order to be added to {@code orders}. <b>IDs must be unique, and the callee is expected to provide a valid, non-duplicate ID.</b>
     * @param status The status of the order. Recognized statuses by other functions include {@code "completed"}, {@code "void"}, and {@code "discard"}.
     * @param order_total The total the order comes to, including the tip. The function ensures no floating point error occurs in adding the total to the new row.
     * @param tip The decimal representation of the percentage tip on the order (0.00, 0.15, 0.20, 0.25, etc.).
     * @param id_drink The ID of the drink chosen for the order.
     * @param id_topping1 The ID of the first topping chosen for the order. If {@code null}, no toppings were chosen for the order.
     * @param id_topping2 The ID of the second topping chosen for the order. If {@code null}, no second topping was chosen for the order.
     * @param ice_level The amount of ice chosen for the order. {@code 0} means {@code "none"}, {@code 1} means {@code "light"}, {@code 2} means {@code "regular"}.
     * @param sugar_level The amount of sugar chosen for the order. {@code 0} means {@code 0%}, {@code 1} means {@code 25%}, {@code 2} means {@code 50%}, {@code 3} means {@code 75%}, {@code 4} means {@code 100%}.
     * @param hot_chosen If the drink was chosen to be served hot ({@code true}) or cold ({@code false}).
     */
    // Insert a new order into the orders table with the provided details.
    // Used to record completed orders in the database.
    public static void insert_order_item(int order_id, String status, double order_total, double tip, int id_drink, int id_topping1, int id_topping2, int ice_level, int sugar_level, boolean hot_chosen) {
        if (!open_connection()) {
            return;
        }
        String sql = "INSERT INTO orders (id_order, status, time_created_at, total_spent, id_employee, tip, id_drink, id_topping1, id_topping2, ice_level, sugar_level, hot_chosen) VALUES (?,?,?,?,?, ?,?,?,?,?, ?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            BigDecimal price;
            BigDecimal tip_amount;
            price = BigDecimal.valueOf(order_total);
            tip_amount = BigDecimal.valueOf(tip);

            stmt.setInt(1, order_id);

            // Orders are completed when they are added into the database.
            stmt.setString(2, status);

            // Created time.
            stmt.setObject(3, OffsetDateTime.now());

            stmt.setBigDecimal(4, price);
            // Possible future feature -- add ability to set employee who completed order for current orders.
            stmt.setInt(5, 0);
            stmt.setBigDecimal(6, tip_amount);
            stmt.setInt(7, id_drink);
            stmt.setInt(8, id_topping1);
            stmt.setInt(9, id_topping2);
            stmt.setInt(10, ice_level);
            stmt.setInt(11, sugar_level);
            stmt.setBoolean(12, hot_chosen);

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        close_connection();
    }

    /**
     * <b>Private helper function.</b><br>
     * Formats/cleans a modifiable argument used in constructing a modifiable SQL statement to prevent SQL injection and incorrect interpretation of null values.
     * A null value in java is known as {@code null}. PostgreSQL formats null values as {@code NULL}. This function will translate a null value in java to the
     * correct, differing PostgreSQL representation.
     * A string value in PostgreSQL can be formatted to be enclosed with {@code '} and {@code '}. All items inside the apostrophes will be interpreted as one string.
     * Ensures valid string values in Java are parsed as-is, and ensures these values are <b>only</b> read as strings by PostgreSQL.
     * Prevents SQL injection.
     *
     * @param value A java value that is to be appended to a string constituting a SQL query.
     * @return The "cleaned" string which will be appended to the SQL query--it can <b>only</b> be read as a string by PostgreSQL. If the string held {@code null}, a string with value {@code "NULL"} is returned. PostgreSQL treats {@code NULL} in queries as the null value.
     */
    // Helper function to clean SQL values, returns "NULL" for null values 
    // and properly escaping single quotes for non-null strings.
    private static String sql_value(String value) {
        return value == null ? "NULL" : "'" + value.replace("'", "''") + "'";
    }

    /**
     * Inserts a new row into the {@code employees} table with the specified attributes.
     * This new row represents a new employee. The employee's {@code id_employee} will be the largest available.
     *
     * @param name The name of the employee.
     * @param position The employee's position.
     * @param phone The employee's phone number.
     * @param pay_rate The employee's hourly pay rate.
     * @param hours The total number of hours the employee is scheduled to work during the current week.
     * @return {@code true} if the employee was successfully added to the table. {@code false} if they were not.
     */
    // Add a new employee to the database with the provided details.
    public static boolean add_employee(String name, String position, String phone,
                                   BigDecimal pay_rate, int hours) {
        String sql = "INSERT INTO employees "
                + "(id_employee, name, position, phone_number, current_pay_rate, hours_worked_for_week) "
                + "VALUES ((SELECT COALESCE(MAX(id_employee), 0) + 1 FROM employees), "
                + sql_value(name) + ", " + sql_value(position) + ", " + sql_value(phone) + ", "
                + pay_rate.toPlainString() + ", " + hours + ")";
        return issue_query_type_update(sql);
    }

    /**
     * Updates an existing row in the {@code employees} table that has the provided employee ID with the specified attributes.
     * All parameters of this function must have valid arguments when called. Parameters have no default arguments.
     *
     * @param id The ID which matches the {@code id_employee} of the employee whose attributes will be changed.
     * @param name The name of the employee.
     * @param position The employee's position.
     * @param phone The employee's phone number.
     * @param pay The employee's hourly pay rate.
     * @param hours The total number of hours the employee is scheduled to work during the current week.
     * @return {@code true} if the employee's attributes were successfully updated. {@code false} if they were not, or if no employee has the specified ID.
     */
    // Update an existing employee's details in the database based on their ID.
    public static boolean update_employee(int id, String name, String position, String phone,
                                      BigDecimal pay, int hours) {
        String sql = "UPDATE employees SET name = " + sql_value(name)
                + ", position = " + sql_value(position)
                + ", phone_number = " + sql_value(phone)
                + ", current_pay_rate = " + pay.toPlainString()
                + ", hours_worked_for_week = " + hours
                + " WHERE id_employee = " + id;
        return issue_query_type_update(sql);
    }

    /**
     * Removes a row representing an employee in the {@code employees} table.
     *
     * @param id The ID which matches the {@code id_employee} of the employee to remove from the table.
     * @return {@code true} if the employee's with the specified ID was removed from the table. {@code false} if they were not, or if no employee has the specified ID.
     */
    // Delete an employee from the database based on their ID.
    public static boolean delete_employee(int id) {
        return issue_query_type_update("DELETE FROM employees WHERE id_employee = " + id);
    }

    /**
     * Inserts a new row into the {@code menu_drinks} table with the specified attributes.
     * This new row represents a new drink. The drink's {@code id_drink} will be the largest available.
     * Ingredients needed to make the drink are provided with a list, and {@code join_menu_drinks_and_inv_edible} is updated such that the database stores which ingredients are needed to make the newly added drink.
     *
     * @param name The name of the drink.
     * @param price The base price of the drink (cost to order the drink with no additional toppings).
     * @param type The type or "category" the drink falls under.
     * @param hot_available {@code true} if the drink can be served hot, {@code false} if not.
     * @param is_non_caffeinated {@code true} if the drink is non-caffeinated, {@code false} if it is caffeinated.
     * @param edible_ids A {@link List} of IDs, where IDs match with {@code id_edible}s in the {@code inv_edible} table for ingredients needed to make the base drink.
     * @return {@code true} if the drink was successfully added to {@code menu_drinks} and the ingredient associations in the join table were correctly added. {@code false} if this isn't the case.
     */
    // Add a new drink to the database with the provided details, along with its associated ingredients
    // to the join_menu_drinks_and_inv_edible table. 
    // Returns true if the drink and its ingredients were successfully added, false otherwise.
    public static boolean add_drink_with_ingredients(
            String name, BigDecimal price, String type,
            boolean hot_available, boolean is_non_caffeinated,
            List<Integer> edible_ids) {
        String drink_sql = "INSERT INTO menu_drinks "
                + "(id_drink, name, price, type, hot_available, is_non_caffeinated) "
                + "VALUES ((SELECT COALESCE(MAX(id_drink), 0) + 1 FROM menu_drinks), "
                + sql_value(name) + ", " + price.toPlainString() + ", " + sql_value(type) + ", "
                + hot_available + ", " + is_non_caffeinated + ")";
        if (!issue_query_type_update(drink_sql)) {
            return false;
        }

        List<MenuDrinksRowDTO> drinks = issue_query_type_rows_MenuDrinks(
                "SELECT * FROM menu_drinks WHERE name = " + sql_value(name)
                        + " ORDER BY id_drink DESC LIMIT 1");
        if (drinks.isEmpty()) {
            return false;
        }
        int drink_id = drinks.get(0).id_drink();
        for (int edible_id : edible_ids) {
            String ingredient_sql = "INSERT INTO join_menu_drinks_and_inv_edible "
                    + "(id_join_menu_drinks_and_inv_edible, id_drink, id_edible) "
                    + "VALUES ((SELECT COALESCE(MAX(id_join_menu_drinks_and_inv_edible), 0) + 1 "
                    + "FROM join_menu_drinks_and_inv_edible), " + drink_id + ", " + edible_id + ")";
            if (!issue_query_type_update(ingredient_sql)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates an existing row in the {@code menu_drinks} table that has the provided drink ID with the specified attributes.
     * All parameters of this function must have valid arguments when called. Parameters have no default arguments.
     * Ingredients needed to make the drink are provided with a list, and {@code join_menu_drinks_and_inv_edible} is updated such that the database stores which ingredients are needed to make the newly added drink.
     * Note that old associations of ingredients to the drink with the provided drink ID in {@code join_menu_drinks_and_inv_edible} are discarded preceding the update.
     *
     * @param drink_id The ID of the drink ({@code id_drink}) that is to be updated.
     * @param name The name of the drink.
     * @param price The base price of the drink (cost to order the drink with no additional toppings).
     * @param type The type or "category" the drink falls under.
     * @param hot_available {@code true} if the drink can be served hot, {@code false} if not.
     * @param is_non_caffeinated {@code true} if the drink is non-caffeinated, {@code false} if it is caffeinated.
     * @param edible_ids A {@link List} of IDs, where IDs match with {@code id_edible}s in the {@code inv_edible} table for ingredients needed to make the base drink.
     * @return {@code true} if the drink in {@code menu_drinks} and the ingredient associations in the join table were successfully updated. {@code false} if this isn't the case.
     */
    // Update an existing drink's details in the database based on its ID, along with its associated ingredients
    // in the join_menu_drinks_and_inv_edible table. 
    // Returns true if the drink and its ingredients were successfully updated, false otherwise.
    public static boolean update_drink_with_ingredients(
            int drink_id, String name, BigDecimal price, String type,
            boolean hot_available, boolean is_non_caffeinated,
            List<Integer> edible_ids) {
        String drink_sql = "UPDATE menu_drinks SET name = " + sql_value(name)
                + ", price = " + price.toPlainString() + ", type = " + sql_value(type)
                + ", hot_available = " + hot_available + ", is_non_caffeinated = "
                + is_non_caffeinated + " WHERE id_drink = " + drink_id;
        if (!issue_query_type_update(drink_sql)
                || !issue_query_type_update(
                        "DELETE FROM join_menu_drinks_and_inv_edible WHERE id_drink = " + drink_id)) {
            return false;
        }

        for (int edible_id : edible_ids) {
            String ingredient_sql = "INSERT INTO join_menu_drinks_and_inv_edible "
                    + "(id_join_menu_drinks_and_inv_edible, id_drink, id_edible) "
                    + "VALUES ((SELECT COALESCE(MAX(id_join_menu_drinks_and_inv_edible), 0) + 1 "
                    + "FROM join_menu_drinks_and_inv_edible), " + drink_id + ", " + edible_id + ")";
            if (!issue_query_type_update(ingredient_sql)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes a row representing a drink in the {@code menu_drinks} table.
     * All associations of ingredients in the {@code inv_edible} table needed to make this topping as expressed in the {@code join_menu_toppings_and_inv_edible} table will be removed.
     * Additionally, all ingredients in the {@code inv_edible} table needed to make this topping will be removed from that table.
     *
     * @param drink_id The ID which matches the {@code id_drink} of the drink to remove from the {@code menu_drinks} table.
     * @return {@code true} if the drink with the specified ID was removed from the table and its ingredient associations were removed from the join table. {@code false} if this isn't the case.
     */
    public static boolean delete_drink(int drink_id) {
        if(!issue_query_type_update("DELETE FROM menu_drinks WHERE id_drink = " + drink_id)){ 
            return false;
        }
        return issue_query_type_update(
                "DELETE FROM join_menu_drinks_and_inv_edible WHERE id_drink = " + drink_id);
    }

    /**
     * Inserts a new row into the {@code menu_toppings} table with the specified attributes.
     * This new row represents a new topping. The topping's {@code id_topping} will be the largest available.
     * The ingredient needed to make the new topping is considered to be itself, of the same name. {@code join_menu_toppings_and_inv_edible} is updated such that the database stores the ingredient needed to make the newly added topping.
     *
     * @param name The name of the topping. Simultaneously, this is also the name of the ingredient needed to make the topping.
     * @param price The price of the topping (additional cost to add the topping to an order).
     *
     * @return {@code true} if the topping was successfully added to {@code menu_toppings} and the ingredient association was correctly added to the join table. {@code false} if this isn't the case.
     */
    // Add a new topping to the database with the provided details, along with its associated inventory item
    // to the inv_edible table and the join_menu_toppings_and_inv_edible table.
    // Returns true if the topping and its inventory item were successfully added, false otherwise.
    public static boolean add_topping_with_inventory(String name, BigDecimal price) {
        if (!issue_query_type_update(
                "INSERT INTO menu_toppings (id_topping, name, price) "
                + "VALUES ((SELECT COALESCE(MAX(id_topping), 0) + 1 FROM menu_toppings), "
                + sql_value(name) + ", " + price.toPlainString() + ")")) {
            return false;
        }

        List<MenuToppingsRowDTO> toppings = issue_query_type_rows_MenuToppings(
                "SELECT * FROM menu_toppings WHERE name = " + sql_value(name)
                        + " ORDER BY id_topping DESC LIMIT 1");
        if (toppings.isEmpty()) {
            return false;
        }
        int topping_id = toppings.get(0).id_topping();

        if (!issue_query_type_update(
                "INSERT INTO inv_edible (id_edible, name, amount_servings) "
                + "VALUES ((SELECT COALESCE(MAX(id_edible), 0) + 1 FROM inv_edible), "
                + sql_value(name) + ", 400)")) {
            return false;
        }
        List<InvEdibleRowDTO> ingredients = issue_query_type_rows_InvEdible(
                "SELECT * FROM inv_edible WHERE name = " + sql_value(name)
                        + " ORDER BY id_edible DESC LIMIT 1");
        if (ingredients.isEmpty()) {
            return false;
        }
        int edible_id = ingredients.get(0).id_edible();

        return issue_query_type_update(
                "INSERT INTO join_menu_toppings_and_inv_edible "
                + "(id_join_menu_toppings_and_inv_edible, id_topping, id_edible) "
                + "VALUES ((SELECT COALESCE(MAX(id_join_menu_toppings_and_inv_edible), 0) + 1 "
                + "FROM join_menu_toppings_and_inv_edible), " + topping_id + ", " + edible_id + ")");
    }

    /**
     * Updates an existing row in the {@code menu_toppings} table that has the provided topping ID with the specified attributes.
     * All parameters of this function must have valid arguments when called. Parameters have no default arguments.
     * The ingredient needed to make the new topping is considered to be itself, of the same name. {@code join_menu_toppings_and_inv_edible} is updated such that the database stores the ingredient needed to make the newly added topping.
     * Note that the old association of an ingredient to the topping with the provided drink ID in {@code join_menu_toppings_and_inv_edible} is discarded preceding the update.
     *
     * @param name The name of the topping. Simultaneously, this is also the name of the ingredient needed to make the topping.
     * @param price The price of the topping (additional cost to add the topping to an order).
     *
     * @return {@code true} if the topping in {@code menu_toppings} was successfully updated along with its ingredient association in the join table. {@code false} if this isn't the case.
     */
    public static boolean update_topping_with_inventory(
            int topping_id, String name, BigDecimal price) {
        if (!issue_query_type_update(
                "UPDATE menu_toppings SET name = " + sql_value(name)
                + ", price = " + price.toPlainString()
                + " WHERE id_topping = " + topping_id)) {
            return false;
        }

        return issue_query_type_update(
                "UPDATE inv_edible SET name = " + sql_value(name)
                + " WHERE id_edible = (SELECT id_edible "
                + "FROM join_menu_toppings_and_inv_edible WHERE id_topping = " + topping_id + ")");
    }

    /**
     * <b>Private helper function.</b><br>
     * Changes the amount of an item (ingredient) used to make a topping in either the {@code inv_edible} or {@code inv_nonedible} tables.
     *
     * @param inv_id The ID (either {@code inv_edible} or {@code inv_nonedible} of the item.
     * @param val The new amount (or amount of servings) the item is to have.
     * @param type Specification of if the item is in {@code inv_edible} or {@code inv_nonedible}. If {@code "inv_edible"}, the item is said to be in {@code inv_edible}. If {@code "inv_nonedible"}, the item is said to be in {@code inv_nonedible}.
     * @return {@code true} If the amount of the item was successfully changed to the specified value. {@code false} if the amount of the item was not successfully changed, or if the ID is not in either of the inventory tables.
     * @throws IllegalArgumentException Thrown if {@code type} is neither {@code "inv_edible"} nor {@code "inv_nonedible"}.
     */
    private static boolean modify_topping_quantity_in_inventory(int inv_id, int val, String type) {
        if(type.equals("inv_edible")){

            if (!issue_query_type_update(
                    "UPDATE inv_edible SET amount_servings = " + val
                    + " WHERE id_edible = " + inv_id)) {
                return false;
            }
            return true;
        }
        else if(type.equals("inv_nonedible")) {
            if (!issue_query_type_update(
                    "UPDATE inv_nonedible SET amount = " + val
                    + " WHERE id_nonedible = " + inv_id)) {
                return false;
            }
            return true;
        }
        else{throw new IllegalArgumentException("Invalid inventory type: " + type);}
    }

    /**
     * Modifies the amount of an item (ingredient) in the {@code inv_edible} table given its ID.
     *
     * @param edible_id The ID of the item in the {@code inv_edible} table.
     * @param val The new amount of servings the stock of the item is expected to constitute. <b>SPECIAL CASE: </b> If set to {@code -1}, the existing amount is decremented. Preventions against impossible decrementation (ex: decrementing an amount 0) must be handled by the callee.
     * @return {@code true} If the amount of the item was successfully changed to the specified value. {@code false} if the amount of the item was not successfully changed, or if the ID is not in the inventory table.
     */
    // Modifies the quantity of an edible inventory item to the input value.
    // If you simply want to decrement, input -1.
    public static boolean modify_quantity_in_edible_inventory(int edible_id, int val){
        if(val == -1){
            val= PGComms.issue_query_type_rows_InvEdible("SELECT * FROM inv_edible WHERE id_edible = " + edible_id).getFirst().amount_servings() - 1;
        }
        return modify_topping_quantity_in_inventory(edible_id, val, "inv_edible");
    }


    /**
     * Modifies the amount of an item in the {@code inv_nonedible} table given its ID.
     *
     * @param nonedible_id The ID of the item in the {@code inv_nonedible} table.
     * @param val The new amount the item is to have. <b>SPECIAL CASE: </b> If set to {@code -1}, the existing amount is decremented. Preventions against impossible decrementation (ex: decrementing an amount 0) must be handled by the callee.
     * @return {@code true} If the amount of the item was successfully changed to the specified value. {@code false} if the amount of the item was not successfully changed, or if the ID is not in the inventory table.
     */
    // Modifies the quantity of a nonedible inventory item to the input value.
    // If you simply want to decrement, input -1.
    public static boolean modify_quantity_in_nonedible_inventory(int nonedible_id, int val){
        if(val == -1){
            return issue_query_type_update(
                    "UPDATE inv_nonedible SET amount = amount - 1 "
                    + "WHERE id_nonedible = " + nonedible_id
                    + " AND amount > 0");
        }
        return modify_topping_quantity_in_inventory(nonedible_id, val, "inv_nonedible");
    }

    /**
     * Modifies the status of an order in the {@code orders} table given its ID.
     *
     * @param order_id The ID of the order in the {@code orders} table.
     * @param status The new status of the order. Recognized statuses by other functions include {@code "completed"}, {@code "void"}, and {@code "discard"}.
     * @return {@code true} If the status of the order was successfully changed to the specified value. {@code false} if the status was not successfully changed, or if the ID is not in the order table.
     */
    public static boolean modify_order_status_in_orders(int order_id, String status) {
        if (!issue_query_type_update(
                "UPDATE orders SET status = " + sql_value(status)
                        + " WHERE id_order = " + order_id)) {
            return false;
        }
        return true;
    }

    /**
     * Removes a row representing a topping in the {@code menu_toppings} table.
     * All associations of ingredients in the {@code inv_edible} table needed to make this topping as expressed in the {@code join_menu_toppings_and_inv_edible} table will be removed.
     * Additionally, these ingredients needed to make the topping will be removed from {@code inv_edible}.
     *
     * @param topping_id The ID which matches the {@code id_topping} of the topping to remove from the {@code menu_toppings} table.
     * @return {@code true} if the topping with the specified ID was removed from the table, its ingredient associations were removed from the join table, and the ingredients themselves were removed from the inventory table. {@code false} if this isn't the case.
     */
    public static boolean delete_topping(int topping_id) {
        if(!issue_query_type_update("DELETE FROM menu_toppings WHERE id_topping = " + topping_id)){ 
            return false;
        }
        if(!issue_query_type_update("DELETE FROM inv_edible WHERE id_edible = (SELECT id_edible "
                + "FROM join_menu_toppings_and_inv_edible WHERE id_topping = " + topping_id + ")")) {
            return false;
        }
        return issue_query_type_update(
                "DELETE FROM join_menu_toppings_and_inv_edible WHERE id_topping = " + topping_id);
    }

    /**
     * Inserts a new row into the {@code inv_edible} table with the specified attributes.
     * This new row represents a new edible item in the edible inventory. The edible item's {@code id_edible} will be the largest available.
     *
     * @param name The name of the edible item.
     * @param quantity The amount of servings the edible item constitutes (consumption of 1 amount per serving) to add to the inventory.
     *
     * @return {@code true} if the edible item was successfully added to {@code inv_edible}. {@code false} if this isn't the case.
     */
    // Add a new edible item to the database with the provided details.
    public static boolean add_edible_item_to_inventory(String name, int quantity) {
        String sql = "INSERT INTO inv_edible (id_edible, name, amount_servings) "
                + "VALUES ((SELECT COALESCE(MAX(id_edible), 0) + 1 FROM inv_edible), "
                + sql_value(name) + ", " + quantity + ")";
        return issue_query_type_update(sql);
    }

    /**
     * Inserts a new row into the {@code inv_nonedible} table with the specified attributes.
     * This new row represents a new non-edible item in the non-edible inventory. The non-edible item's {@code id_nonedible} will be the largest available.
     *
     * @param name The name of the non-edible item.
     * @param quantity The amount non-edible item constitutes (consumption of 1 amount per serving) to add to the inventory.
     *
     * @return {@code true} if the non-edible item was successfully added to {@code inv_nonedible}. {@code false} if this isn't the case.
     */
    // Add a new nonedible item to the database with the provided details.
    public static boolean add_nonedible_item_to_inventory(String name, int quantity) {
        String sql = "INSERT INTO inv_nonedible (id_nonedible, name, amount) "
                + "VALUES ((SELECT COALESCE(MAX(id_nonedible), 0) + 1 FROM inv_nonedible), "
                + sql_value(name) + ", " + quantity + ")";
        return issue_query_type_update(sql);
    }
}