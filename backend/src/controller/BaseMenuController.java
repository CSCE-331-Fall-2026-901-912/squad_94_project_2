package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import model.CurrentOrderModel;
import model.MenuModel;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import database.PGComms;

// This class serves as a base controller for the cashier-related views in the application. 
// It provides common functionality for managing the current order, displaying the order total, and opening various windows related to adding drinks, clearing orders, finishing orders, and accessing the manager view.

/**
 * This is the controller for the base launch menu.
 * Controller intended for handling attaching the cancellation menu, tipping menu, and manager main menu.
 * This controller makes use of 4 initialization calls for separate windows, and handles updating the order list. * 
 *
 * @author Squad 94 - Asher Blevins
 */
public class BaseMenuController {

    @FXML private TextArea current_order;
    @FXML private Label order_total;
    @FXML private Button add_drink_button;

    private final CurrentOrderModel order_model;
    private final MenuModel menu_model;

    /**
     * Constructor for the BaseMenuController class.
     * Initializes the controller with the provided order model and menu model.
     * @param order_model The CurrentOrderModel instance used to manage the current order and its total.
     * @param menu_model The MenuModel instance used to manage the menu items and their details.
     */
    public BaseMenuController(CurrentOrderModel order_model, MenuModel menu_model) {
        this.order_model = order_model;
        this.menu_model = menu_model;
    }

    /**
     * Initializes the controller by binding the current order and order total to the corresponding properties in the order model.
     * Also updates the state of the "Add Drink" button based on the availability of required
     */
    @FXML public void initialize() {
        current_order.textProperty().bind(order_model.get_current_order());
        order_total.textProperty().bind(order_model.get_order_total());
        update_add_drink_button();
    }

    /**
     * Opens the "Add Drink" window when the corresponding button is clicked.
     * If required non-edible inventory items are not available, a warning alert is displayed instead
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML public void open_add_drink(ActionEvent event) {
        if (!PGComms.are_required_nonedible_items_available(1)) {
            update_add_drink_button();
            return;
        }

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

    /**
     * Updates the state of the "Add Drink" button based on the availability of required non-edible inventory items.
     * If any required items are out of stock, a warning alert is displayed to inform the user that drinks cannot be added.
     * This method is called during initialization and can also be called whenever the inventory state changes to ensure that the button's availability reflects the current inventory status.
     */
    private void update_add_drink_button() {
        boolean available = PGComms.are_required_nonedible_items_available(1);
        if (!available) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Drinks cannot be added because one or more required non-edible inventory items (IDs 1-4) are out of stock.")
                    .showAndWait();
        }
    }

    /**
     * Opens the "Cancel Order" window when the corresponding button is clicked.
     * This method creates a new instance of the CancellationController and uses the ViewLoader to open the cancellation window. If any exceptions occur during this process, they are printed to the console.
     * @param event The ActionEvent triggered by the button click.
     */
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

    /**
     * Opens the "Finish Order" window when the corresponding button is clicked.
     * This method creates a new instance of the TipController and uses the ViewLoader to open the tipping window. If any exceptions occur during this process, they are printed to the console.
     * @param event The ActionEvent triggered by the button click.
     */
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

    /**
     * Opens the "Manager View" window when the corresponding button is clicked.
     * This method creates a new instance of the ManagerController and uses the ViewLoader to open the manager view window. If any exceptions occur during this process, they are printed to the console.
     * @param event The ActionEvent triggered by the button click.
     */
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
