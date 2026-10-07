package model;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.ManagerController;

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

    @FXML public ProcessingOrders open_proc_orders(ManagerController manager_controller){
        try {
            Stage stage_proc_orders = new Stage();
            FXMLLoader proc_orders_popup = new FXMLLoader(getClass().getResource("/gui/manager/ProcessingOrders.fxml"));

            ProcessingOrders proc_popup_model = new ProcessingOrders(stage_proc_orders);
            proc_orders_popup.setController(proc_popup_model);

            Parent root = proc_orders_popup.load();
            stage_proc_orders.setScene(new Scene(root));
            stage_proc_orders.show();

            proc_popup_model.load_orders();
            return proc_popup_model;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ProcessingOrders(null);
    }

    @FXML public InventoryScreen open_inventory(ManagerController manager_controller){
        try {
            Stage stage_inv = new Stage();
            FXMLLoader inv_popup = new FXMLLoader(getClass().getResource("/gui/manager/InventoryScreen.fxml"));
            inv_popup.setController(manager_controller);

            Parent root = inv_popup.load();
            Scene scene = new Scene(root);

            stage_inv.setScene(scene);
            stage_inv.show();

            InventoryScreen inv_popup_model = new InventoryScreen(stage_inv);
            manager_controller.initialize(inv_popup_model);
            return inv_popup_model;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new InventoryScreen(null);
    } 
}
