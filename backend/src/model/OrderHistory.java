package model;

import dto.MenuDrinksRowDTO;
import dto.OrdersRowDTO;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import database.PGComms;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import javafx.scene.control.ComboBox;


public class OrderHistory {
    @FXML private TableView<OrdersRowDTO> order_table;
    @FXML private TableColumn<OrdersRowDTO, Integer> order_id;
    @FXML private TableColumn<OrdersRowDTO, String> order_name;
    @FXML private TableColumn<OrdersRowDTO, BigDecimal> order_total;
    @FXML private TextField searchOH;
    @FXML private ComboBox<String> sortByOH;
    @FXML private ComboBox<String> timePeriodOH;

    private final Stage stage;
    private final Map<Integer, String> drink_names = new HashMap<>();
    private FilteredList<OrdersRowDTO> filtered;
    private SortedList<OrdersRowDTO> sorted;

    public OrderHistory() { this(null); }
    public OrderHistory(Stage stage) { this.stage = stage; }

    public void close() {
        if (stage != null) stage.close();
    }

     public void load_orders() {
        drink_names.clear();
        for (MenuDrinksRowDTO d : PGComms.issue_query_type_rows_MenuDrinks("SELECT * FROM menu_drinks")) {
            drink_names.put(d.id_drink(), d.name());
        }

        order_id.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().id_order()));
        order_name.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(drink_name(d.getValue())));
        order_total.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().total_spent()));
        setup_row_click();

        ObservableList<OrdersRowDTO> all_orders = FXCollections.observableArrayList(PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order"));
        filtered = new FilteredList<>(all_orders, o -> true);
        sorted = new SortedList<>(filtered);
        order_table.setItems(sorted);

        sortByOH.getItems().setAll("Order ID (low to high)", "Order ID (high to low)", "Drink name (A-Z)", "Total (low to high)", "Total (high to low)", "Newest first");
        sortByOH.getSelectionModel().selectFirst();

        timePeriodOH.getItems().setAll("All time", "Today", "Last 7 days", "Last 30 days");
        timePeriodOH.getSelectionModel().selectFirst();

        searchOH.textProperty().addListener((o, a, b) -> apply_filters());
        timePeriodOH.valueProperty().addListener((o, a, b) -> apply_filters());
        sortByOH.valueProperty().addListener((o, a, b) -> apply_sort());
        apply_sort();
    }

    private String drink_name(OrdersRowDTO orders) {
        return drink_names.getOrDefault(orders.id_drink(), "Unknown drink (" + orders.id_drink() + ")");
    }

    private void apply_filters() {
        String text = searchOH.getText() == null ? "" : searchOH.getText().trim().toLowerCase();
        String period = timePeriodOH.getValue();
        filtered.setPredicate(o -> matches_search(o, text) && in_period(o, period));
    }

    private boolean matches_search(OrdersRowDTO orders, String str) {
        if (str.isEmpty()) return true;
        return String.valueOf(orders.id_order()).contains(str) || drink_name(orders).toLowerCase().contains(str) || String.valueOf(orders.total_spent()).contains(str);
    }

    private boolean in_period(OrdersRowDTO orders, String period) {
        if (period == null || period.equals("All time")) return true;
        OffsetDateTime time = order_time(orders);
        if (time == null) return false;

        LocalDate day = time.atZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();
        return switch (period) {
            case "Today" -> day.equals(today);
            case "Last 7 days" -> !day.isBefore(today.minusDays(6));
            case "Last 30 days" -> !day.isBefore(today.minusDays(29));
            default -> true;
        };
    }

    private void apply_sort() {
        String choice = sortByOH.getValue();
        if (choice == null) return;
        Comparator<OrdersRowDTO> c = switch (choice) {
            case "Order ID (high to low)" -> Comparator.comparingInt(OrdersRowDTO::id_order).reversed();
            case "Drink name (A-Z)" -> Comparator.comparing(o -> drink_name(o).toLowerCase());
            case "Total (low to high)" -> Comparator.comparing(OrdersRowDTO::total_spent);
            case "Total (high to low)" -> Comparator.comparing(OrdersRowDTO::total_spent).reversed();
            case "Newest first" -> Comparator.comparing(this::order_time, Comparator.nullsLast(Comparator.reverseOrder()));
            default -> Comparator.comparingInt(OrdersRowDTO::id_order);
        };
        sorted.setComparator(c);
    }

    private void setup_row_click(){
        order_table.setRowFactory(tv -> {
            TableRow <OrdersRowDTO> row = new TableRow<>();
            return row;
        });
    }

        private OffsetDateTime order_time(OrdersRowDTO order) {
        return order.time_created_at();
    } 
}