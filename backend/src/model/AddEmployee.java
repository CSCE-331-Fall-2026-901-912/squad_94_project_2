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

    @FXML private Label title_label;
    @FXML private TextField name_field;
    @FXML private TextField position_field;
    @FXML private TextField phone_field;
    @FXML private TextField pay_field;
    @FXML private TextField hours_field;
    @FXML private Button submit_button;

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
        title_label.setText("Edit Employee");
        submit_button.setText("Save");
        name_field.setText(existing.name());
        position_field.setText(existing.position());
        phone_field.setText(existing.phone_number() == null ? "" : existing.phone_number());
        pay_field.setText(existing.current_pay_rate() == null ? "" : existing.current_pay_rate().toPlainString());
        hours_field.setText(String.valueOf(existing.hours_worked_for_week()));
    }

    @FXML public void submit(ActionEvent event) {
        String name = name_field.getText().trim();
        String position = position_field.getText().trim();
        String phone = phone_field.getText().trim();

        BigDecimal pay;
        int hours;
        try {
            pay = new BigDecimal(pay_field.getText().trim());
            hours = Integer.parseInt(hours_field.getText().trim());
        } catch (NumberFormatException e) {
            show_warning("Pay must be a number (like 12.50) and hours must be a whole number.");
            return;
        }

        if (name.isEmpty() || position.isEmpty()) {
            show_warning("Name and position are required.");
            return;
        }
        if (pay.signum() < 0 || hours < 0) {
            show_warning("Pay and hours can't be negative.");
            return;
        }

        boolean ok = (existing == null)
            ? PGComms.add_employee(name, position, phone, pay, hours)
            : PGComms.update_employee(existing.id_employee(), name, position, phone, pay, hours);

        if (!ok) {
            show_warning("The employee could not be saved. Check the console for the database message.");
            return;
        }

        on_added.run();                                    // reload the table
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    private void show_warning(String message) {
        new Alert(Alert.AlertType.WARNING, message).showAndWait();
    }
}