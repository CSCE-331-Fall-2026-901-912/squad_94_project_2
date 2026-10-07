package model;

import dto.EmployeesRowDTO;
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

public class OrderHistory {
    @FXML private TableView<OrdersRowDTO> order_table;
    @FXML private TableColumn<OrdersRowDTO, Integer> order_id;
    @FXML private TableColumn<OrdersRowDTO, Integer> order_name;
    @FXML private TableColumn<OrdersRowDTO, BigDecimal> order_total;

    private final Stage stage;

    public OrderHistory() { this(null); }
    public OrderHistory(Stage stage) { this.stage = stage; }

    public void close() {
        if (stage != null) stage.close();
    }

    public void load_orders() {
        order_id.setCellValueFactory(d    -> new ReadOnlyObjectWrapper<>(d.getValue().id_order()));
        order_name.setCellValueFactory(d  -> new ReadOnlyObjectWrapper<>(d.getValue().id_drink()));
        order_total.setCellValueFactory(d   -> new ReadOnlyObjectWrapper<>(d.getValue().total_spent()));
        setup_row_click();
        order_table.setItems(FXCollections.observableArrayList(PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order")));
        // order_table.setPlaceholder(new javafx.scene.control.Label("No orders loaded"));
    }

    private void setup_row_click(){
        order_table.setRowFactory(tv -> {
            TableRow <OrdersRowDTO> row = new TableRow<>();
            return row;
        });
    }
}