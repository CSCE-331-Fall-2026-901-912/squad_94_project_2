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

public class OrderHistoryModel {
    private final Map<Integer, String> drink_names = new HashMap<>();
    private final Map<Integer, String> edible_names = new HashMap<>();

    public List<OrdersRowDTO> get_orders() {
        return PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order");
    }

    public void load_drink_names() {
        drink_names.clear();
        for (MenuDrinksRowDTO d : PGComms.issue_query_type_rows_MenuDrinks("SELECT * FROM menu_drinks")) {
            drink_names.put(d.id_drink(), d.name());
        }
    }

    public void load_edible_names() {
        edible_names.clear();
        for (InvEdibleRowDTO d : PGComms.issue_query_type_rows_InvEdible("SELECT * FROM inv_edible")) {
            edible_names.put(d.id_edible(), d.name());
        }
    }

    public String drink_name(OrdersRowDTO order) {
        return drink_names.getOrDefault(order.id_drink(), "Unknown drink (" + order.id_drink() + ")");
    }


    public String drink_name(Integer id) {
        return drink_names.getOrDefault(id, "Unknown drink (" + id + ")");
    }

    public String edible_name(Integer id) {
        return edible_names.getOrDefault(id, "Unknown edible (" + id + ")");
    }

    public boolean matches_search(OrdersRowDTO orders, String str) {
        if (str.isEmpty()) return true;
        return String.valueOf(orders.id_order()).contains(str) || drink_name(orders).toLowerCase().contains(str) || String.valueOf(orders.total_spent()).contains(str);
    }

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