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
    @FXML private Label title_label;
    @FXML private TextField name_field;
    @FXML private TextField price_field;
    @FXML private Button submit_button;

    private final Runnable on_saved;
    private final MenuToppingsRowDTO existing;

    public AddMenuTopping(Runnable on_saved, MenuToppingsRowDTO existing) {
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
                ? PGComms.add_topping_with_inventory(name, price)
                : PGComms.update_topping_with_inventory(existing.id_topping(), name, price);
        if (!saved) {
            show_error("The topping could not be saved.");
            return;
        }

        on_saved.run();
        ((Node) submit_button).getScene().getWindow().hide();
    }

    private void show_error(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}