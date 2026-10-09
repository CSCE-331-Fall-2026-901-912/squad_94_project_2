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

public class TipController {
    private final CurrentOrderModel order_model;

    @FXML
    private Label tip_total;

    private BigDecimal tip_rate = BigDecimal.ZERO;

    public TipController(CurrentOrderModel order_model) {
        this.order_model = order_model;
    }

    @FXML
    private void initialize() {
        update_total_display();
    }

    @FXML
    public void tip0() {
        set_tip_rate(BigDecimal.ZERO);
    }

    @FXML
    public void tip10() {
        set_tip_rate(new BigDecimal("0.10"));
    }

    @FXML
    public void tip15() {
        set_tip_rate(new BigDecimal("0.15"));
    }

    @FXML
    public void tip20() {
        set_tip_rate(new BigDecimal("0.20"));
    }

    @FXML
    public void tip25() {
        set_tip_rate(new BigDecimal("0.25"));
    }

    private void set_tip_rate(BigDecimal rate) {
        if (rate.signum() < 0) {
            throw new IllegalArgumentException(
                "Tip rate cannot be negative.");
        }

        tip_rate = rate;
        update_total_display();
    }

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

    @FXML
    public void tipdone(ActionEvent event) {
        final boolean checkout_succeeded;

        try {
            checkout_succeeded = order_model.checkout(tip_rate);
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

    private void show_warning(String message) {
        new Alert(
            Alert.AlertType.WARNING,
            message
        ).showAndWait();
    }
}