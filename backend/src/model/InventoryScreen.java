package model;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import controller.ManagerController;
import javafx.scene.Scene;

public class InventoryScreen {

    private final Stage stage;

    public InventoryScreen() { this(null); }
    public InventoryScreen(Stage stage) { this.stage = stage; }

    public void close() {
        if (stage != null) stage.close();
    }

    @FXML public ItemDetailsPopUp item_click(ManagerController manager_controller, String item_name) {
        try{
            Stage stage_item = new Stage();
            FXMLLoader item_details = new FXMLLoader(getClass().getResource("/gui/manager/ItemDetailsPopUp.fxml"));
            ItemDetailsPopUp item_details_model = new ItemDetailsPopUp();
            item_details.setController(manager_controller);

            Parent root = item_details.load();
            Scene scene = new Scene(root);

            stage_item.setScene(scene);
            stage_item.show();
            return item_details_model;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ItemDetailsPopUp();
    }
}