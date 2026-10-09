package database;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import dto.*;
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

    // A lot of these SQL queries were created before the generic query functions were implemented, so they are not using the generic functions
    // They will eventually be refactored to use the generic functions at a later date
    
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
    public static List<IDNameValDTO> get_out_of_stock() {
        List<InvNonEdibleRowDTO> inv_non_edible_select = issue_query_type_rows_InvNonEdible("SELECT * FROM inv_nonedible WHERE amount = 0 LIMIT 3");
        List<InvEdibleRowDTO> inv_edible_select = issue_query_type_rows_InvEdible("SELECT * FROM inv_edible WHERE amount_servings = 0 LIMIT 3");
        return get_inv_selection_common(inv_non_edible_select, inv_edible_select);
    }

    public static List<IDNameValDTO> get_running_low_on() {
        List<InvNonEdibleRowDTO> inv_non_edible_select = issue_query_type_rows_InvNonEdible("SELECT * FROM inv_nonedible WHERE amount > 0 ORDER BY amount LIMIT 3");
        List<InvEdibleRowDTO> inv_edible_select = issue_query_type_rows_InvEdible("SELECT * FROM inv_edible WHERE amount_servings > 0 ORDER BY amount_servings LIMIT 3");
        return get_inv_selection_common(inv_non_edible_select, inv_edible_select);
    }

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

    public static BigDecimal get_sales_total() {
        BigDecimal price = BigDecimal.ZERO;

        String sql = "SELECT SUM(total_spent) AS sum_total FROM ( SELECT total_spent FROM orders WHERE completed = TRUE)";

        if (!open_connection()) {
            return price;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    public static BigDecimal get_sales_total_date(String date_input) {
        BigDecimal price = BigDecimal.ZERO;

        String sql = "SELECT SUM(total_spent) AS sum_total FROM ( SELECT total_spent FROM orders WHERE completed = TRUE AND time_completed_at >= ? AND time_completed_at < ?)";

        if (!open_connection()) {
            return price;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDate day = LocalDate.parse(date_input);
            stmt.setObject(1, day.atStartOfDay());
            stmt.setObject(2, day.plusDays(1).atStartOfDay());
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
        String sql = "INSERT INTO orders (id_order, completed, time_created_at, time_completed_at, total_spent, id_employee, tip, id_drink, id_topping1, id_topping2, ice_level, sugar_level, hot_chosen) VALUES (?,?,?,?,?, ?,?,?,?,?, ?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            BigDecimal price;
            BigDecimal tip_amount;
            price = BigDecimal.valueOf(order_total);
            tip_amount = BigDecimal.valueOf(tip);

            stmt.setInt(1, order_id);

            // We take an order to be completed when it is added into the database.
            stmt.setBoolean(2, true);

            // Created time.
            Timestamp right_now = Timestamp.valueOf(LocalDateTime.now());
            stmt.setTimestamp(3, right_now);

            // Time completed (we take an order to be completed when it is added into the database).
            stmt.setTimestamp(4, right_now);

            stmt.setBigDecimal(5, price);
            // TODO: GET CURRENT EMPLOYEE ID
            stmt.setInt(6, 0);
            stmt.setBigDecimal(7, tip_amount);
            stmt.setInt(8, id_drink);
            stmt.setInt(9, id_topping1);
            stmt.setInt(10, id_topping2);
            stmt.setInt(11, ice_level);
            stmt.setInt(12, sugar_level);
            stmt.setBoolean(13, hot_chosen);

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        close_connection();
    }


    private static String sql_value(String value) {
        return value == null ? "NULL" : "'" + value.replace("'", "''") + "'";
    }

    public static boolean add_employee(String name, String position, String phone,
                                   BigDecimal pay_rate, int hours) {
        String sql = "INSERT INTO employees "
                + "(id_employee, name, position, phone_number, current_pay_rate, hours_worked_for_week) "
                + "VALUES ((SELECT COALESCE(MAX(id_employee), 0) + 1 FROM employees), "
                + sql_value(name) + ", " + sql_value(position) + ", " + sql_value(phone) + ", "
                + pay_rate.toPlainString() + ", " + hours + ")";
        return issue_query_type_update(sql);
    }

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

    public static boolean delete_employee(int id) {
        return issue_query_type_update("DELETE FROM employees WHERE id_employee = " + id);
    }

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

    public static boolean delete_drink(int drink_id) {
        if(!issue_query_type_update("DELETE FROM menu_drinks WHERE id_drink = " + drink_id)){ 
            return false;
        }
        return issue_query_type_update(
                "DELETE FROM join_menu_drinks_and_inv_edible WHERE id_drink = " + drink_id);

    }

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


    private static boolean modify_topping_quantity_in_inventory(int topping_id, int val, String type) {
        if(type.equals("inv_edible")){
            // ArrayList<JoinMenuToppingsAndInvEdibleRowDTO> edible_rows;
            // edible_rows = PGComms.issue_query_type_rows_JoinMenuToppingsAndInvEdible("SELECT * FROM join_menu_toppings_and_inv_edible WHERE id_topping = " + topping_id);
            // int edible_id = edible_rows.getFirst().id_edible();

            if (!issue_query_type_update(
                    "UPDATE inv_edible SET amount_servings = " + val
                    + " WHERE id_edible = " + topping_id)) {
                return false;
            }
            return true;
        }
        else if(type.equals("inv_nonedible")) {
            if (!issue_query_type_update(
                    "UPDATE inv_nonedible SET amount = " + val
                    + " WHERE id_nonedible = " + topping_id)) {
                return false;
            }
            return true;
        }
        else{throw new IllegalArgumentException("Invalid inventory type: " + type);}
    }

    /**
     * Modifies the quantity of a edible inventory item to the input value.
     * If you simply want to decrement, input -1.
     */
    public static boolean modify_topping_quantity_in_edible_inventory(int edible_id, int val){
        if(val == -1){
            val= PGComms.issue_query_type_rows_InvEdible("SELECT * FROM inv_edible WHERE id_edible = " + edible_id).getFirst().amount_servings() - 1;
        }
        return modify_topping_quantity_in_inventory(edible_id, val, "inv_edible");
    }
    /**
     * Modifies the quantity of a nonedible inventory item to the input value.
     * If you simply want to decrement, input -1.
     */
    public static boolean modify_topping_quantity_in_nonedible_inventory(int nonedible_id, int val){
        if(val == -1){
            val= PGComms.issue_query_type_rows_InvNonEdible("SELECT * FROM inv_nonedible WHERE id_nonedible = " + nonedible_id).getFirst().amount() - 1;
        }
        return modify_topping_quantity_in_inventory(nonedible_id, val, "inv_nonedible");
    }

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
}