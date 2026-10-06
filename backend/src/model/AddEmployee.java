package model;

import database.PGComms;
import java.math.BigDecimal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AddEmployee {

    @FXML private TextField nameField;
    @FXML private TextField positionField;
    @FXML private TextField phoneField;
    @FXML private TextField payField;
    @FXML private TextField hoursField;

    private final Runnable on_added;                     

    public AddEmployee(Runnable on_added) {
        this.on_added = on_added;
    }

    @FXML public void submit(ActionEvent event) {
        String name = nameField.getText().trim();
        String position = positionField.getText().trim();
        String phone = phoneField.getText().trim();

        BigDecimal pay;
        int hours;
        try {
            pay = new BigDecimal(payField.getText().trim());
            hours = Integer.parseInt(hoursField.getText().trim());
        } catch (NumberFormatException e) {
            warn("Pay must be a number (like 12.50) and hours must be a whole number.");
            return;
        }

        if (name.isEmpty() || position.isEmpty()) {
            warn("Name and position are required.");
            return;
        }
        if (pay.signum() < 0 || hours < 0) {
            warn("Pay and hours can't be negative.");
            return;
        }

        if (!PGComms.add_employee(name, position, phone, pay, hours)) {
            warn("The employee could not be saved. Check the console for the database message.");
            return;
        }

        on_added.run();                                    // reload the table
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    private void warn(String message) {
        new Alert(Alert.AlertType.WARNING, message).showAndWait();
    }
}