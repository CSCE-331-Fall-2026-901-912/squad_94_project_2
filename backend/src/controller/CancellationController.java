package controller;
import model.CurrentOrderModel;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Window;

public class CancellationController {
    private final CurrentOrderModel order_model;

    public CancellationController(CurrentOrderModel order_model) {
        this.order_model = order_model;
    }
    // CANCELLATION POP UP BUTTONS
    @FXML public void confirm_cancel(ActionEvent event){
        order_model.clear();
        close_window(event);
    }
    @FXML public void deny_cancel(ActionEvent event){
        close_window(event);
    }

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
