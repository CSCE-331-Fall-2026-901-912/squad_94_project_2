package model;

import database.PGComms;
import dto.InvEdibleRowDTO;
import dto.MenuDrinksRowDTO;
import dto.OrdersRowDTO;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Model class for managing order history-related data, including drinks, toppings, associated prices, IDs, and the times at which each order was created.
 *
 * @author Squad 94 - Natalie Gonzalez
 */
public class OrderHistoryModel {
    private final Map<Integer, String> drink_names = new HashMap<>();
    private final Map<Integer, String> edible_names = new HashMap<>();
 
    /**
     * Method for retrieving all orders from the database
     */
    public List<OrdersRowDTO> get_orders() {
        return PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order");
    }

    /**
     * Loads drink ids and names into hashmap.
     */
    public void load_drink_names() {
        drink_names.clear();
        for (MenuDrinksRowDTO d : PGComms.issue_query_type_rows_MenuDrinks("SELECT * FROM menu_drinks")) {
            drink_names.put(d.id_drink(), d.name());
        }
    }
    
    /**
     * Method for putting all edible names into the edible_names map
     */
    public void load_edible_names() {
        edible_names.clear();
        for (InvEdibleRowDTO d : PGComms.issue_query_type_rows_InvEdible("SELECT * FROM inv_edible")) {
            edible_names.put(d.id_edible(), d.name());
        }
    }

    /**
     * Returns the drink name based on the drink ID.
     *
     * @param order DTO for retrieving information row-by-row from the orders table in the SQL database.
     */
    public String drink_name(OrdersRowDTO order) {
        return drink_names.getOrDefault(order.id_drink(), "Unknown drink (" + order.id_drink() + ")");
    }

    /**
     * Retrieves the name of a drink given its ID
     *
     * @param id Drink ID
     */
    public String drink_name(Integer id) {
        return drink_names.getOrDefault(id, "Unknown drink (" + id + ")");
    }

    /**
     * Retrieves the name of an edible given its ID
     *
     * @param id Edible item ID
     */
    public String edible_name(Integer id) {
        return edible_names.getOrDefault(id, "Unknown edible (" + id + ")");
    }

    /**
     * Returns rows associated with input from the search bar
     *
     * @param orders DTO for retrieving information row-by-row from the orders table in the SQL database.
     * @param str Search bar user input
     */
    public boolean matches_search(OrdersRowDTO orders, String str) {
        if (str.isEmpty()) return true;
        return String.valueOf(orders.id_order()).contains(str) || drink_name(orders).toLowerCase().contains(str) || String.valueOf(orders.total_spent()).contains(str);
    }

    /**
     * Checks timestamp and sorts it into the appropriate time period.
     *
     * @param orders DTO for retrieving information row-by-row from the orders table in the SQL database.
     * @param period Time period filter option
     */
    public boolean in_period(OrdersRowDTO order,String period) {
        if (period == null || period.equals("All time")) {
            return true;
        }
        OffsetDateTime time = order_time(order);

        if (time == null) {
            return false;
        }
        LocalDate day = time.atZoneSameInstant(ZoneId.systemDefault()).toLocalDate();

        LocalDate today = LocalDate.now();

        return switch (period) {
            case "Today" -> day.equals(today);
            case "Last 7 days" -> !day.isBefore(today.minusDays(6));
            case "Last 30 days" -> !day.isBefore(today.minusDays(29));
            default -> true;
        };
    }

    /**
     * Duplicate method for checking if an order is within a specified period, using a specified start and end time
     *
     * @param orders DTO for retrieving information row-by-row from the orders table in the SQL database.
     * @param start Start date from user input
     * @param end End date from user input
     */
    public boolean in_period(OrdersRowDTO order, OffsetDateTime start, OffsetDateTime end) {
        if (start == null || end == null) {
            return false;
        }
        OffsetDateTime time = order_time(order);

        if (time == null) {
            return false;
        }
        
        if(time.isAfter(start) && time.isBefore(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sorts drinks based on the filters chosen on GUI.
     *
     * @param choice Filter on GUI
     */
    public Comparator<OrdersRowDTO> comparator_for(String choice) {
        return switch (choice) {
            case "Order ID (high to low)" -> Comparator.comparingInt(OrdersRowDTO::id_order).reversed();
            case "Drink name (A-Z)" -> Comparator.comparing(o -> drink_name(o).toLowerCase());
            case "Total (low to high)" -> Comparator.comparing(OrdersRowDTO::total_spent);
            case "Total (high to low)" -> Comparator.comparing(OrdersRowDTO::total_spent).reversed();
            case "Newest first" -> Comparator.comparing(this::order_time, Comparator.nullsLast(Comparator.reverseOrder()));
            default -> Comparator.comparingInt(OrdersRowDTO::id_order);
        };
    }

    /**
     * Returns the creation timestamp
     *
     * @param order DTO for retrieving information row-by-row from the orders table in the SQL database.
     */
    private OffsetDateTime order_time(OrdersRowDTO order) {
        return order.time_created_at();
    }

    /**
     * Modifies an order's order status
     *
     * @param order_id item_ID
     * @param status status the order will change to
     */
    public boolean change_order_status(int order_id, String status) {
        return PGComms.modify_order_status_in_orders(order_id, status);
    }
}