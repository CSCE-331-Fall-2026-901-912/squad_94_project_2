package controller;

import java.math.BigDecimal;
import java.util.Comparator;

import database.PGComms;
import dto.OrdersRowDTO;
import model.OrderHistoryModel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class OrderHistoryController {
    private final OrderHistoryModel model;

    @FXML private TableView<OrdersRowDTO> order_table;
    @FXML private TableColumn<OrdersRowDTO, Integer> order_id;
    @FXML private TableColumn<OrdersRowDTO, String> order_name;
    @FXML private TableColumn<OrdersRowDTO, BigDecimal> order_total;
    @FXML private TextField searchOH;
    @FXML private ComboBox<String> sortByOH;
    @FXML private ComboBox<String> timePeriodOH;

    private FilteredList<OrdersRowDTO> filtered;
    private SortedList<OrdersRowDTO> sorted;

    public OrderHistoryController(OrderHistoryModel model) {
        this.model = model;
    }

    @FXML public void initialize() {
        model.load_drink_names();

        order_id.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().id_order()));
        order_name.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(model.drink_name(d.getValue())));
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

    private void apply_filters() {
        String text = searchOH.getText() == null ? "" : searchOH.getText().trim().toLowerCase();
        String period = timePeriodOH.getValue();
        filtered.setPredicate(o -> model.matches_search(o, text) && model.in_period(o, period));
    }

    private void apply_sort() {
        String choice = sortByOH.getValue();
        if (choice == null) return;
        Comparator<OrdersRowDTO> c = model.comparator_for(choice);
        sorted.setComparator(c);
    }

    private void setup_row_click(){
        order_table.setRowFactory(tv -> {
            TableRow <OrdersRowDTO> row = new TableRow<>();
            return row;
        });
    }
}
