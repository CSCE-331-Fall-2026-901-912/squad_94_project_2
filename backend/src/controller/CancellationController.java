package controller;
import model.CurrentOrderModel;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Window;

/**
 * This is the controller for the cancellation pop-up window.
 * Controller class for handling order cancellation actions in the application. This class manages the user interactions related to confirming
 * or denying the cancellation of an order, and ensures that the cancellation process is handled cleanly by closing the associated pop-up window.
 * 
 * @author Squad 94 - Asher Blevins
 */

public class CancellationController {
    private final CurrentOrderModel order_model;

    /**
     * Constructor for the CancellationController class.
     * Initializes the controller with the provided order model.
     * @param order_model The CurrentOrderModel instance used to manage the current order and its total.
     */
    public CancellationController(CurrentOrderModel order_model) {
        this.order_model = order_model;
    }
    // CANCELLATION POP UP BUTTONS

    /**
     * Handles the confirmation of order cancellation when the corresponding button is clicked.
     * This method clears the current order in the order model and closes the cancellation pop-up window.
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML public void confirm_cancel(ActionEvent event){
        order_model.clear();
        close_window(event);
    }

    /**
     * Handles the denial of order cancellation when the corresponding button is clicked.
     * This method simply closes the cancellation pop-up window without making any changes to the current order.
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML public void deny_cancel(ActionEvent event){
        close_window(event);
    }

    // Handle closing of any pop-up window involved with the ordering process.
    // Ensure the pop-up is closed cleanly, and that cancellation is tied to the cancellation window.

    /**
     * Closes the pop-up window associated with the cancellation process.
     * This method retrieves the source of the event, checks if it is attached to a window, and then hides the window to close the pop-up.
     * If the source is not attached to a window, an IllegalStateException is thrown.
     * @param event The ActionEvent triggered by the button click.
     * @throws IllegalStateException if the event source is not a JavaFX node or if the node is not attached to a window.
     */
    private void close_window(ActionEvent event) {
        Node source = (Node) event.getSource();
        if (source.getScene() == null || source.getScene().getWindow() == null) {
            throw new IllegalStateException(
                "Cancellation popup is not attached to a window.");
        }

        Window window = source.getScene().getWindow();
        window.hide();
    }
}
