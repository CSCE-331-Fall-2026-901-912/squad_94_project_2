package model;

import database.PGComms;
import dto.EmployeesRowDTO;

import java.math.BigDecimal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class AddEmployee {

    @FXML private Label titleLabel;
    @FXML private TextField nameField;
    @FXML private TextField positionField;
    @FXML private TextField phoneField;
    @FXML private TextField payField;
    @FXML private TextField hoursField;
    @FXML private Button submitButton;

    private final Runnable on_added;  
    private final EmployeesRowDTO existing;                  

    public AddEmployee(Runnable on_added) {
        this.on_added = on_added;
        this.existing = null;
    }

    public AddEmployee(Runnable on_added, EmployeesRowDTO existing) {
        this.on_added = on_added;
        this.existing = existing;
    }

    @FXML private void initialize() {                       // runs after the fields are injected
        if (existing == null) return;
        titleLabel.setText("Edit Employee");
        submitButton.setText("Save");
        nameField.setText(existing.name());
        positionField.setText(existing.position());
        phoneField.setText(existing.phone_number() == null ? "" : existing.phone_number());
        payField.setText(existing.current_pay_rate() == null ? "" : existing.current_pay_rate().toPlainString());
        hoursField.setText(String.valueOf(existing.hours_worked_for_week()));
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

        boolean ok = (existing == null)
            ? PGComms.add_employee(name, position, phone, pay, hours)
            : PGComms.update_employee(existing.id_employee(), name, position, phone, pay, hours);

        if (!ok) {
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