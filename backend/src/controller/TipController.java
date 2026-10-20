package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Window;
import model.CurrentOrderModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This is the controller for the tip pop-up window.
 * Controller class for handling tip selection and order completion actions in the application. 
 * This class manages the user interactions related to selecting a tip percentage, calculating the total amount including the tip, and finalizing the order checkout process.
 * It ensures that the tip is applied correctly and that the order is processed cleanly, with appropriate error handling and user feedback in case of any issues during checkout.
 * 
 * @author Squad 94 - Asher Blevins
 */
public class TipController {
    private final CurrentOrderModel order_model;

    @FXML
    private Label tip_total;

    // The current tip rate selected by the user, represented as a decimal (e.g., 0.15 for 15%).
    private BigDecimal tip_rate = BigDecimal.ZERO;

    /**
     * Constructor for the TipController class.
     * Initializes the controller with the provided order model.
     * @param order_model The CurrentOrderModel instance used to manage the current order and its total.
     */
    public TipController(CurrentOrderModel order_model) {
        this.order_model = order_model;
    }

    /**
     * Initializes the controller by updating the total display to reflect the current order total and the selected tip rate.
     * This method is called automatically after the FXML fields have been injected and the controller is ready to be used.
     * It ensures that the total amount displayed to the user is accurate and up-to-date.
     */
    @FXML private void initialize() {
        update_total_display();
    }

    // The following methods handle the selection of different tip rates by the user.

    /**
     * Sets the tip rate to 0% when the corresponding button is clicked.
     * This method updates the tip rate to zero and refreshes the total display accordingly.
     */
    @FXML
    public void tip0() {
        set_tip_rate(BigDecimal.ZERO);
    }

    /**
     * Sets the tip rate to 10% when the corresponding button is clicked.
     * This method updates the tip rate to 0.10 and refreshes the total display accordingly.
     */
    @FXML
    public void tip10() {
        set_tip_rate(new BigDecimal("0.10"));
    }

    /**
     * Sets the tip rate to 15% when the corresponding button is clicked.
     * This method updates the tip rate to 0.10 and refreshes the total display accordingly.
     */
    @FXML
    public void tip15() {
        set_tip_rate(new BigDecimal("0.15"));
    }

    /**
     * Sets the tip rate to 20% when the corresponding button is clicked.
     * This method updates the tip rate to 0.10 and refreshes the total display accordingly.
     */
    @FXML
    public void tip20() {
        set_tip_rate(new BigDecimal("0.20"));
    }

    /**
     * Sets the tip rate to 25% when the corresponding button is clicked.
     * This method updates the tip rate to 0.10 and refreshes the total display accordingly.
     */
    @FXML
    public void tip25() {
        set_tip_rate(new BigDecimal("0.25"));
    }

    // Sets the tip rate and updates the total display accordingly. Throws an exception if the rate is negative.

    /**
     * Sets the tip rate to the specified value and updates the total display accordingly.
     * Throws an IllegalArgumentException if the provided rate is negative.
     * @param rate The new tip rate to be set, represented as a decimal (e.g., 0.15 for 15%).
     * @throws IllegalArgumentException if the provided rate is negative.
     */
    private void set_tip_rate(BigDecimal rate) {
        if (rate.signum() < 0) {
            throw new IllegalArgumentException(
                "Tip rate cannot be negative.");
        }

        tip_rate = rate;
        update_total_display();
    }

    /**
     * Updates the total display to reflect the current order total and the selected tip rate.
     * This method calculates the subtotal, tip amount, and total amount, and updates the tip_total label accordingly.
     */
    private void update_total_display() {
        BigDecimal subtotal = order_model
            .get_total()
            .setScale(2, RoundingMode.HALF_UP);

        BigDecimal tip_amount = subtotal
            .multiply(tip_rate)
            .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = subtotal
            .add(tip_amount)
            .setScale(2, RoundingMode.HALF_UP);

        tip_total.setText("Total: " + total);
    }

    // Handles the completion of the order, including processing the tip and finalizing the checkout. 
    // If any errors occur during checkout, a warning is displayed to the user from here.

    /**
     * Handles the completion of the order when the corresponding button is clicked.
     * This method attempts to checkout the current order with the selected tip rate. If the checkout fails, a warning alert is displayed to the user with the error message.
     * If the checkout succeeds, the current order is cleared and the tip pop-up window is closed.
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    public void tipdone(ActionEvent event) {
        final boolean checkout_succeeded;

        try {
            checkout_succeeded = order_model.checkout(tip_rate, false);
        } catch (RuntimeException exception) {
            String message = exception.getMessage();

            if (message == null || message.isBlank()) {
                message = "An unexpected checkout error occurred.";
            }

            show_warning(
                "The order could not be completed: " + message);
            return;
        }

        if (!checkout_succeeded) {
            show_warning(
                "The order could not be completed. "
                + "No order was submitted.");
            return;
        }

        order_model.clear();
        close_window(event);
    }

    /**
     * Closes the tip pop-up window associated with the tip selection process.
     * This method retrieves the source of the event, checks if it is attached to a window, and then hides the window to close the pop-up. If the source is not attached to a window, an IllegalStateException is thrown.
     * @param event The ActionEvent triggered by the button click.
     * @throws IllegalStateException if the event source is not a JavaFX node or if the node is not attached to a window.
     */
    private void close_window(ActionEvent event) {
        if (!(event.getSource() instanceof Node source)) {
            throw new IllegalStateException(
                "Tip event source is not a JavaFX node.");
        }

        if (source.getScene() == null
                || source.getScene().getWindow() == null) {
            throw new IllegalStateException(
                "Tip popup is not attached to a window.");
        }

        Window window = source.getScene().getWindow();
        window.hide();
    }

    /**
     * Displays a warning alert with the specified message.
     * This method creates and shows a warning alert dialog to inform the user of any issues or errors that occurred during the tip selection or checkout process.
     * @param message The warning message to be displayed in the alert dialog.
     */
    private void show_warning(String message) {
        new Alert(
            Alert.AlertType.WARNING,
            message
        ).showAndWait();
    }
}