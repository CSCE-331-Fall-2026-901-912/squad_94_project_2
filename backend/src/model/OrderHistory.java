package model;

import dto.EmployeesRowDTO;
import dto.MenuDrinksRowDTO;
import dto.OrdersRowDTO;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import database.PGComms;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class OrderHistory {
    @FXML private TableView<OrdersRowDTO> order_table;
    @FXML private TableColumn<OrdersRowDTO, Integer> order_id;
    @FXML private TableColumn<OrdersRowDTO, String> order_name;
    @FXML private TableColumn<OrdersRowDTO, BigDecimal> order_total;
    @FXML private TextField searchOH;

    private final Stage stage;
    private final Map<Integer ,String> drink_names = new HashMap<>();

    public OrderHistory() { this(null); }
    public OrderHistory(Stage stage) { this.stage = stage; }

    public void close() {
        if (stage != null) stage.close();
    }

    public void load_orders() {
        Map<Integer, String> drink_names = new HashMap<>();
        for (MenuDrinksRowDTO d : PGComms.issue_query_type_rows_MenuDrinks("SELECT * FROM menu_drinks")) {
            drink_names.put(d.id_drink(), d.name());
        }

        order_id.setCellValueFactory(d    -> new ReadOnlyObjectWrapper<>(d.getValue().id_order()));
        order_name.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(drink_names.getOrDefault(d.getValue().id_drink(), "Unknown drink (" + d.getValue().id_drink() + ")")));
        order_total.setCellValueFactory(d   -> new ReadOnlyObjectWrapper<>(d.getValue().total_spent()));
        setup_row_click();
        // order_table.setItems(FXCollections.observableArrayList(PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order")));
        // order_table.setPlaceholder(new javafx.scene.control.Label("No orders loaded"));

        ObservableList<OrdersRowDTO> all_orders = FXCollections.observableArrayList(PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order"));
        FilteredList<OrdersRowDTO> filtered_orders = new FilteredList<>(all_orders, o -> true);
        SortedList<OrdersRowDTO> sorted_orders = new SortedList<>(filtered_orders);
        sorted_orders.comparatorProperty().bind(order_table.comparatorProperty());
        order_table.setItems(sorted_orders);

        searchOH.textProperty().addListener((obs, old, text) -> {
            String q = (text == null) ? "" : text.trim().toLowerCase();
            filtered_orders.setPredicate(o -> {
                if (q.isEmpty()) return true;
                String name = drink_names.getOrDefault(o.id_drink(), "").toLowerCase();
                return String.valueOf(o.id_order()).contains(q) || name.contains(q) || String.valueOf(o.total_spent()).contains(q);
            });
        });
    }

    private void setup_row_click(){
        order_table.setRowFactory(tv -> {
            TableRow <OrdersRowDTO> row = new TableRow<>();
            return row;
        });
    }
}