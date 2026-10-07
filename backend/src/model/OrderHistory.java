package model;

import database.PGComms;
import dto.OrdersRowDTO;
import java.lang.reflect.RecordComponent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.stage.Stage;

public class OrderHistory {

    // One order row, trimmed to what the screens display.
    public record OrderSummary(int id, boolean flag, OffsetDateTime placed, BigDecimal total) {
        public String placed_text() {
            if (placed == null) return "";
            return placed.atZoneSameInstant(ZoneId.systemDefault())
                         .format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
        }
        public boolean is_today() {
            return placed != null && placed.atZoneSameInstant(ZoneId.systemDefault())
                                           .toLocalDate().equals(LocalDate.now());
        }
        public String search_text() {
            return id + " " + placed_text() + " " + String.format("%.2f", total);
        }
    }

    // Read a row DTO's field by position (works when the DTO is a record, like EmployeesRowDTO).
    public static Object field(Object row, int index) {
        try {
            RecordComponent[] parts = row.getClass().getRecordComponents();
            if (parts == null || index >= parts.length) return null;
            return parts[index].getAccessor().invoke(row);
        } catch (Exception e) {
            System.out.println("Could not read field " + index + ": " + e);
            return null;
        }
    }

    // Orders columns by position: 0 id, 1 boolean flag, 2 order time, 4 total.
    private static OrderSummary from(OrdersRowDTO r) {
        Object id = field(r, 0), flag = field(r, 1), placed = field(r, 2), total = field(r, 4);
        return new OrderSummary(
            id instanceof Integer i ? i : 0,
            flag instanceof Boolean b && b,
            placed instanceof OffsetDateTime t ? t : null,
            total instanceof BigDecimal d ? d : BigDecimal.ZERO);
    }

    // Every order, newest first, using PGComms' existing orders query.
    public static List<OrderSummary> fetch_orders() {
        List<OrderSummary> list = new ArrayList<>();
        for (OrdersRowDTO r : PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY 1 DESC")) {
            list.add(from(r));
        }
        return list;
    }

    private final Stage stage;
    private final ObservableList<OrderSummary> orders = FXCollections.observableArrayList();
    private final FilteredList<OrderSummary> visible = new FilteredList<>(orders, o -> true);

    private String search_text = "";
    private boolean today_only = false;
    private String sort_mode = "Newest first";

    public OrderHistory() { this(null); }
    public OrderHistory(Stage stage) { this.stage = stage; }

    // The list the TableView should show (already filtered).
    public ObservableList<OrderSummary> get_orders() { return visible; }

    public void load_orders() {
        orders.setAll(fetch_orders());
        apply_sort();
    }

    public void filter(String text) {
        search_text = (text == null) ? "" : text.trim().toLowerCase();
        refresh_filter();
    }

    public void set_period(String period) {
        today_only = "Today".equals(period);
        refresh_filter();
    }

    public void sort_by(String mode) {
        if (mode != null) sort_mode = mode;
        apply_sort();
    }

    private void refresh_filter() {
        visible.setPredicate(o ->
            (!today_only || o.is_today())
            && (search_text.isEmpty() || o.search_text().toLowerCase().contains(search_text)));
    }

    private void apply_sort() {
        Comparator<OrderSummary> by_id = Comparator.comparingInt(OrderSummary::id);
        Comparator<OrderSummary> by_total = Comparator.comparing(OrderSummary::total);
        Comparator<OrderSummary> c = switch (sort_mode) {
            case "Oldest first"  -> by_id;
            case "Highest total" -> by_total.reversed();
            case "Lowest total"  -> by_total;
            default              -> by_id.reversed();
        };
        FXCollections.sort(orders, c);
    }

    public void close() {
        if (stage != null) stage.close();
    }
}