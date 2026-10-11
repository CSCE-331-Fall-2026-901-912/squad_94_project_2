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

// Model class for managing order history-related data, including drinks, toppings, associated prices, IDs, and the times at which each order was created.
public class OrderHistoryModel {
    private final Map<Integer, String> drink_names = new HashMap<>();
    private final Map<Integer, String> edible_names = new HashMap<>();

    // Method for retrieving all orders from the database
    public List<OrdersRowDTO> get_orders() {
        return PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order");
    }

    // Loads drink ids and names into hashmap.
    public void load_drink_names() {
        drink_names.clear();
        for (MenuDrinksRowDTO d : PGComms.issue_query_type_rows_MenuDrinks("SELECT * FROM menu_drinks")) {
            drink_names.put(d.id_drink(), d.name());
        }
    }

    // Returns the drink name based on the drink ID.
    public String drink_name(OrdersRowDTO order) {
        return drink_names.getOrDefault(order.id_drink(), "Unknown drink (" + order.id_drink() + ")");
    }

    // Method for retrieving the name of a drink given its ID
    public String drink_name(Integer id) {
        return drink_names.getOrDefault(id, "Unknown drink (" + id + ")");
    }

    // Method for retrieving the name of an edible given its ID
    public String edible_name(Integer id) {
        return edible_names.getOrDefault(id, "Unknown edible (" + id + ")");
    }

    
    public boolean matches_search(OrdersRowDTO orders, String str) {
        if (str.isEmpty()) return true;
        return String.valueOf(orders.id_order()).contains(str) || drink_name(orders).toLowerCase().contains(str) || String.valueOf(orders.total_spent()).contains(str);
    }

    // Checks timestamp and sorts it into the appropriate time period.
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

    // Duplicate method for checking if an order is within a specified period, using a specified start and end time
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

    // Sorts drinks based on the filters chosen on GUI.
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

    private OffsetDateTime order_time(OrdersRowDTO order) {
        return order.time_created_at();
    } 
}