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

}