package controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.PGComms;
import dto.MenuDrinksRowDTO;
import dto.OrdersRowDTO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableRow;
import model.OrderHistoryModel;

public class ProdSalesScreenController {

    private record SalesRecord(Integer id_drink, String name_drink, Double total_sales_drink) {}
    private record ProdUseRecord(Integer id_drink, String name_drink, Double inventoryUsed) {}

    // private SalesRecord prod_sales_record = populate_sales_record();
    private HashMap<Integer, Double> id_to_total_sales = new HashMap<>();

    @FXML private TextField from_year;
    @FXML private TextField from_month;
    @FXML private TextField from_day;

    @FXML private TextField to_year;
    @FXML private TextField to_month;
    @FXML private TextField to_day;


    @FXML private TableView<SalesRecord> whole_table;
    @FXML private TableColumn<SalesRecord, Integer> id_column;
    @FXML private TableColumn<SalesRecord, String> name_column;
    @FXML private TableColumn<SalesRecord, Double> total_sales_column;
    // @FXML private TableColumn<ProdSalesRecord, String> inventory_used_column;

    private FilteredList<SalesRecord> filtered;
    private SortedList<SalesRecord> sorted;

    private OrderHistoryModel model;

    
    public ProdSalesScreenController(OrderHistoryModel model) {
        this.model = model;
    }

    // ProdSalesRecord populate_record(){
    //     return new ProdSalesRecord(
    //         0,
    //         "",
    //         "",
    //         ""
    //     );
    // }

    @FXML public void initialize() {
        model.load_drink_names();

        for(int i = 0; i < 20; i++){
            System.out.println("Loaded drink names: " + model.drink_name(i));
        }

        id_column.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().id_drink()));
        name_column.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().name_drink()));
        total_sales_column.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().total_sales_drink()));
        setup_row_click();

        ObservableList<OrdersRowDTO> all_orders = FXCollections.observableArrayList(PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order"));


        for (OrdersRowDTO order : all_orders) {
            id_to_total_sales.put(order.id_drink(), id_to_total_sales.getOrDefault(order.id_drink(), 0.0) + order.total_spent().doubleValue());
        }

        ObservableList<SalesRecord> sale_orders = FXCollections.observableArrayList();

        for (Integer id_drink : id_to_total_sales.keySet()) {
            sale_orders.add(new SalesRecord(
                id_drink,
                model.drink_name(id_drink),
                id_to_total_sales.get(id_drink)
            ));
        }


        filtered = new FilteredList<>(sale_orders);
        sorted = new SortedList<>(filtered);

        whole_table.setItems(sorted);

        // sortByOH.getItems().setAll("Order ID (low to high)", "Order ID (high to low)", "Drink name (A-Z)", "Total (low to high)", "Total (high to low)", "Newest first");
        // sortByOH.getSelectionModel().selectFirst();

        // timePeriodOH.getItems().setAll("All time", "Today", "Last 7 days", "Last 30 days");
        // timePeriodOH.getSelectionModel().selectFirst();

        // searchOH.textProperty().addListener((o, a, b) -> apply_filters());
        // timePeriodOH.valueProperty().addListener((o, a, b) -> apply_filters());
        // sortByOH.valueProperty().addListener((o, a, b) -> apply_sort());
        // apply_sort();
    }

    // private void apply_filters() {
    //     String text = searchOH.getText() == null ? "" : searchOH.getText().trim().toLowerCase();
    //     String period = timePeriodOH.getValue();
    //     filtered.setPredicate(o -> model.matches_search(o, text) && model.in_period(o, period));
    // }

    // private void apply_sort() {
    //     String choice = sortByOH.getValue();
    //     if (choice == null) return;
    //     Comparator<OrdersRowDTO> c = model.comparator_for(choice);
    //     sorted.setComparator(c);
    // }

    private void setup_row_click(){
        whole_table.setRowFactory(tv -> {
            TableRow <SalesRecord> row = new TableRow<>();
            return row;
        });
    }



    
    private final Map<Integer, String> drink_names = new HashMap<>();

    // public List<OrdersRowDTO> get_orders() {
    //     return PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order");
    // }

    // public void load_drink_names() {
    //     drink_names.clear();
    //     for (MenuDrinksRowDTO d : PGComms.issue_query_type_rows_MenuDrinks("SELECT * FROM menu_drinks")) {
    //         drink_names.put(d.id_drink(), d.name());
    //     }
    // }

    public String drink_name(SalesRecord order) {
        return drink_names.getOrDefault(order.id_drink(), "Unknown drink (" + order.id_drink() + ")");
    }

    // public boolean matches_search(OrdersRowDTO orders, String str) {
    //     if (str.isEmpty()) return true;
    //     return String.valueOf(orders.id_order()).contains(str) || drink_name(orders).toLowerCase().contains(str) || String.valueOf(orders.total_spent()).contains(str);
    // }

    // public boolean in_period(OrdersRowDTO order,String period) {
    //     if (period == null || period.equals("All time")) {
    //         return true;
    //     }
    //     OffsetDateTime time = order_time(order);

    //     if (time == null) {
    //         return false;
    //     }
    //     LocalDate day = time
    //         .atZoneSameInstant(ZoneId.systemDefault())
    //         .toLocalDate();

    //     LocalDate today = LocalDate.now();

    //     return switch (period) {
    //         case "Today" -> day.equals(today);
    //         case "Last 7 days" -> !day.isBefore(today.minusDays(6));
    //         case "Last 30 days" -> !day.isBefore(today.minusDays(29));
    //         default -> true;
    //     };
    // }

    // public Comparator<OrdersRowDTO> comparator_for(String choice) {
    //     return switch (choice) {
    //         case "Order ID (high to low)" -> Comparator.comparingInt(OrdersRowDTO::id_order).reversed();
    //         case "Drink name (A-Z)" -> Comparator.comparing(o -> drink_name(o).toLowerCase());
    //         case "Total (low to high)" -> Comparator.comparing(OrdersRowDTO::total_spent);
    //         case "Total (high to low)" -> Comparator.comparing(OrdersRowDTO::total_spent).reversed();
    //         case "Newest first" -> Comparator.comparing(this::order_time, Comparator.nullsLast(Comparator.reverseOrder()));
    //         default -> Comparator.comparingInt(OrdersRowDTO::id_order);
    //     };
    // }

    // private OffsetDateTime order_time(OrdersRowDTO order) {
    //     return order.time_created_at();
    // } 
}
