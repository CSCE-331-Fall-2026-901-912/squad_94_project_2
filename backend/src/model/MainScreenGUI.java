package model;

import controller.ManagerController;
import database.PGComms;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.time.*;
import database.PGComms.*;

public class MainScreenGUI {
    @FXML public OrderHistory open_order_history(ManagerController manager_controller){
        try {
            Stage stage_oh = new Stage();
            FXMLLoader oh_popup = new FXMLLoader(getClass().getResource("/gui/manager/OrderHistory.fxml"));

            OrderHistory oh_popup_model = new OrderHistory(stage_oh);
            oh_popup.setController(oh_popup_model);

            Parent root = oh_popup.load();
            stage_oh.setScene(new Scene(root));
            stage_oh.show();

            oh_popup_model.load_orders();
            return oh_popup_model;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new OrderHistory(null);
    }

    @FXML public SalesAnalytics open_sales_analytics(ManagerController manager_controller){
        try {
            Stage stage_sales = new Stage();
            FXMLLoader sales_popup = new FXMLLoader(getClass().getResource("/gui/manager/SalesAnalytics.fxml"));
            sales_popup.setController(manager_controller);

            Parent root = sales_popup.load();
            Scene scene = new Scene(root);

            stage_sales.setScene(scene);
            stage_sales.show();

            SalesAnalytics sales_popup_model = new SalesAnalytics(stage_sales);
            manager_controller.initialize(sales_popup_model);
            return sales_popup_model;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new SalesAnalytics(null);
    }

//    @FXML public ProcessingOrders open_proc_orders(ManagerController manager_controller){
//        try {
//            Stage stage_proc_orders = new Stage();
//            FXMLLoader proc_orders_popup = new FXMLLoader(getClass().getResource("/gui/manager/ProcessingOrders.fxml"));
//
//            ProcessingOrders proc_popup_model = new ProcessingOrders(stage_proc_orders);
//            proc_orders_popup.setController(proc_popup_model);
//
//            Parent root = proc_orders_popup.load();
//            stage_proc_orders.setScene(new Scene(root));
//            stage_proc_orders.show();
//
//            proc_popup_model.load_orders();
//            return proc_popup_model;
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ProcessingOrders(null);
//    }
    // MAIN SCREEN
    @FXML
    private AnchorPane InventoryBox;

    @FXML
    private Label InventoryLabel;

    @FXML
    private Label ManagerLabel;

    @FXML
    private TableColumn<?, ?> OOSAmount;

    @FXML
    private Label OOSLabel;

    @FXML
    private TableColumn<?, ?> OOSName;

    @FXML
    private TableView<?> OOSTable;

//    @FXML
//    private AnchorPane ProcOrderBox;
//
//    @FXML
//    private TableColumn<?, ?> ProcOrderID;
//
//    @FXML
//    private TableView<?> ProcOrderTable;
//
//    @FXML
//    private TableColumn<?, ?> ProcOrderTotal;
//
//    @FXML
//    private TableColumn<?, ?> ProcOrders;

    @FXML
    private TableColumn<?, ?> RLAmount;

    @FXML
    private TableColumn<?, ?> RLName;

    @FXML
    private TableView<?> RLTable;

    @FXML
    private AnchorPane SalesBox;

    @FXML
    private Label SalesLabel;

    @FXML
    private AnchorPane TodayOHBox;

    @FXML
    private TableColumn<?, ?> TodayOHID;

    @FXML
    private TableColumn<?, ?> TodayOHOrder;

    @FXML
    private TableView<?> TodayOHTable;

    @FXML
    private TableColumn<?, ?> TodayOHTotal;

    @FXML
    private SplitPane TodaysSales;

    @FXML
    private Label TodaysSalesAmount;

    @FXML
    private Label TodaysSalesLabel;

    @FXML
    private SplitPane TotalSales;

    @FXML
    private Label TotalSalesAmount;

    @FXML
    private Label TotalSalesLabel;
}
