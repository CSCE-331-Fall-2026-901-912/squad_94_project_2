package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import model.CurrentOrderModel;
import model.MenuModel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BaseMenuController {

    @FXML private TextArea current_order;
    @FXML private Label order_total;

    private final CurrentOrderModel order_model;
    private final MenuModel menu_model;

    public BaseMenuController(CurrentOrderModel order_model, MenuModel menu_model) {
        this.order_model = order_model;
        this.menu_model = menu_model;
    }

    @FXML public void initialize() {
        current_order.textProperty().bind(order_model.get_current_order());
        order_total.textProperty().bind(order_model.get_order_total());
    }

    @FXML public void open_add_drink(ActionEvent event) {
        try{
            OrderMenuController controller = new OrderMenuController(order_model, menu_model);

            Stage stage = ViewLoader.open_window(
                "/gui/cashier/OrderMenu.fxml",
                controller,
                current_order.getScene().getWindow());
            
            if (stage != null) {
                stage.setTitle("Add Drink");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML public void clear_order(ActionEvent event){
        try{
            CancellationController controller = new CancellationController(order_model);

            Stage stage = ViewLoader.open_window(
                "/gui/cashier/Cancellation.fxml",
                controller,
                current_order.getScene().getWindow());
            
            if (stage != null) {
                stage.setTitle("Cancel Order");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML public void finish_order(ActionEvent event){
        try{
            TipController controller = new TipController(order_model);

            Stage stage = ViewLoader.open_window(
                "/gui/cashier/TotalPopUp.fxml",
                controller,
                current_order.getScene().getWindow());
            
            if (stage != null) {
                stage.setTitle("Finish Order");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void open_manager_view(ActionEvent event){
        try {
            ManagerController controller = new ManagerController();
            Stage stage = ViewLoader.open_window(
                "/gui/manager/MainScreenGUI.fxml",
                controller,
                ((Node) event.getSource()).getScene().getWindow());

            if (stage != null) {
                stage.setTitle("Manager View");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
