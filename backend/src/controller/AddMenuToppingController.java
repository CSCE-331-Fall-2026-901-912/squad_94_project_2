package controller;

import java.math.BigDecimal;
import dto.MenuToppingsRowDTO;
import model.MenuModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddMenuToppingController {
    @FXML private Label title_label;
    @FXML private TextField name_field;
    @FXML private TextField price_field;
    @FXML private Button submit_button;
    @FXML private Button delete_button;

    private final MenuModel model;
    private final Runnable on_saved;
    private final MenuToppingsRowDTO existing;

    public AddMenuToppingController(MenuModel model, Runnable on_saved, MenuToppingsRowDTO existing) {
        this.model = model;
        this.on_saved = on_saved;
        this.existing = existing;
    }

    @FXML
    private void initialize() {
        if (existing == null) {
            return;
        }
        title_label.setText("Edit Topping");
        submit_button.setText("Save");
        delete_button.setVisible(true);
        delete_button.setManaged(true);
        name_field.setText(existing.name());
        price_field.setText(existing.price().toPlainString());
    }

    @FXML
    private void submit() {
        String name = name_field.getText().trim();
        if (name.isEmpty() || price_field.getText().trim().isEmpty()) {
            show_error("Name and price are required.");
            return;
        }

        BigDecimal price;
        try {
            price = new BigDecimal(price_field.getText().trim());
        } catch (NumberFormatException e) {
            show_error("Price must be a valid number.");
            return;
        }

        boolean saved = existing == null
                ? model.add_topping(name, price)
                : model.update_topping(existing.id_topping(), name, price);
        if (!saved) {
            show_error("The topping could not be saved.");
            return;
        }

        on_saved.run();
        ((Node) submit_button).getScene().getWindow().hide();
    }

    @FXML
    private void delete() {
        if (existing == null) {
            return;
        }

        Alert confirmation = new Alert(
            Alert.AlertType.CONFIRMATION,
            "Delete " + existing.name() + "? This action cannot be undone.",
            ButtonType.CANCEL,
            ButtonType.OK
        );
        confirmation.setTitle("Delete Topping");
        confirmation.setHeaderText(null);

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        if (!model.delete_topping(existing.id_topping())) {
            show_error("The topping could not be deleted.");
            return;
        }

        on_saved.run();
        ((Node) submit_button).getScene().getWindow().hide();
    }

    private void show_error(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}