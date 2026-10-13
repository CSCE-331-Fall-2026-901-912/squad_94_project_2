package controller;

import java.math.BigDecimal;
import java.util.Comparator;

import database.PGComms;
import dto.OrdersRowDTO;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import model.OrderHistoryModel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;

public class OrderHistoryController {
    private final OrderHistoryModel model;

    private static final String WARNING_STYLE =
            "-fx-background-color: #ee0606; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;";

    @FXML private TableView<OrdersRowDTO> order_table;
    @FXML private TableColumn<OrdersRowDTO, Integer> order_id;
    @FXML private TableColumn<OrdersRowDTO, String> order_name;
    @FXML private TableColumn<OrdersRowDTO, BigDecimal> order_total;
    @FXML private TableColumn<OrdersRowDTO, String> order_status;
    @FXML private TextField searchOH;
    @FXML private ComboBox<String> sortByOH;
    @FXML private ComboBox<String> timePeriodOH;
    @FXML private Button void_order_button;
    @FXML private TextField void_order_input;

    private FilteredList<OrdersRowDTO> filtered;
    private SortedList<OrdersRowDTO> sorted;

    public OrderHistoryController(OrderHistoryModel model) {
        this.model = model;
    }

    // Populates the tables, uses the drink ids to pull the drink name information from database, and creates the filtered and sorted lists based on the filters applied.
    @FXML public void initialize() {
        model.load_drink_names();

        order_id.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().id_order()));
        order_name.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(model.drink_name(d.getValue())));
        order_total.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().total_spent()));
        order_status.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().status()));
        setup_row_click();

        void_order_button.setStyle(WARNING_STYLE);

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

    public void refresh_status() {
        initialize();
    }

    // Apply search filter based upon user text input into the OrderHistory page's search bar.
    private void apply_filters() {
        String text = searchOH.getText() == null ? "" : searchOH.getText().trim().toLowerCase();
        String period = timePeriodOH.getValue();
        filtered.setPredicate(o -> model.matches_search(o, text) && model.in_period(o, period));
    }

    // Apply a sorting filter based upon user choice of a button option on the OrderHistory page.
    private void apply_sort() {
        String choice = sortByOH.getValue();
        if (choice == null) return;
        Comparator<OrdersRowDTO> c = model.comparator_for(choice);
        sorted.setComparator(c);
    }

    // Handle click-based behavior on OrderHistory's orders table.
    private void setup_row_click(){
        order_table.setRowFactory(tv -> {
            TableRow <OrdersRowDTO> row = new TableRow<>();
            return row;
        });
    }

    // Handle visual warnings displayed if submit changes button is pressed and some user input is invalid.
    // Otherwise, simply close the add item page to indicate success to the user.
    @FXML
    public void void_order_button_press(ActionEvent event) {
        String text = void_order_input.getText().trim();

        if (text.isEmpty()) {
            show_warning("An order ID must be inputted.");
            return;
        }

        int order_id;
        try {
            order_id = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            show_warning("Order ID must be a valid number.");
            return;
        }

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Void Order " + order_id + "? This action cannot be undone.",
                ButtonType.CANCEL,
                ButtonType.OK
        );
        confirmation.setTitle("Void Order " + order_id + "?");
        confirmation.setHeaderText(null);

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        if (!model.change_order_status(order_id, "void")) {
            show_warning("The status of the order could not be updated.");
            return;
        }
        refresh_status();
    }

    private void show_warning(String message) {
        new Alert(
                Alert.AlertType.WARNING,
                message
        ).showAndWait();
    }
}
