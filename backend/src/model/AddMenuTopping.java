package model;

import java.math.BigDecimal;
import database.PGComms;
import dto.MenuToppingsRowDTO;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddMenuTopping {
    @FXML private Label titleLabel;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private Button submitButton;

    private final Runnable onSaved;
    private final MenuToppingsRowDTO existing;

    public AddMenuTopping(Runnable onSaved, MenuToppingsRowDTO existing) {
        this.onSaved = onSaved;
        this.existing = existing;
    }

    @FXML
    private void initialize() {
        if (existing == null) {
            return;
        }
        titleLabel.setText("Edit Topping");
        submitButton.setText("Save");
        nameField.setText(existing.name());
        priceField.setText(existing.price().toPlainString());
    }

    @FXML
    private void submit() {
        String name = nameField.getText().trim();
        if (name.isEmpty() || priceField.getText().trim().isEmpty()) {
            show_error("Name and price are required.");
            return;
        }

        BigDecimal price;
        try {
            price = new BigDecimal(priceField.getText().trim());
        } catch (NumberFormatException e) {
            show_error("Price must be a valid number.");
            return;
        }

        boolean saved = existing == null
                ? PGComms.add_topping_with_inventory(name, price)
                : PGComms.update_topping_with_inventory(existing.id_topping(), name, price);
        if (!saved) {
            show_error("The topping could not be saved.");
            return;
        }

        onSaved.run();
        ((Node) submitButton).getScene().getWindow().hide();
    }

    private void show_error(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}