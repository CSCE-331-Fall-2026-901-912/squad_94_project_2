package controller;

import java.math.BigDecimal;

import dto.EmployeesRowDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.EmployeeModel;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 * Controller for adding, editing, and deleting employees from the table.
 *
 * @author Squad 94 - Sanjana Ram
 */
public class AddEmployeeController {
    private final EmployeeModel model;
    private final Runnable on_added;
    private final EmployeesRowDTO existing;

    @FXML private Label title_label;
    @FXML private TextField name_field;
    @FXML private TextField position_field;
    @FXML private TextField phone_field;
    @FXML private TextField pay_field;
    @FXML private TextField hours_field;
    @FXML private Button submit_button;
    @FXML private Button delete_button;

    /**
     * Creates an employee form controller.
     *
     * @param model the employee model used to save and delete employee records
     * @param on_saved refresh invoked after an employee is saved or deleted
     * @param existing the employee being edited, or {@code null} when adding an employee
     */
    public AddEmployeeController(EmployeeModel model, Runnable on_saved, EmployeesRowDTO existing) {
        this.model = model;
        this.on_added = on_saved;
        this.existing = existing;
    }

    /**
     * Initializes the form with existing employee data when editing an employee.
     * Otherwise leaves fields blank
     */
    @FXML private void initialize() {            
        if (existing == null) return;
        title_label.setText("Edit Employee");
        submit_button.setText("Save");
        delete_button.setVisible(true);
        delete_button.setManaged(true);
        name_field.setText(existing.name());
        position_field.setText(existing.position());
        phone_field.setText(existing.phone_number() == null ? "" : existing.phone_number());
        pay_field.setText(existing.current_pay_rate() == null ? "" : existing.current_pay_rate().toPlainString());
        hours_field.setText(String.valueOf(existing.hours_worked_for_week()));
    }

    /**
     * Validates the form input and adds or updates an employee record.
     */
    @FXML public void submit() {

        String name = name_field.getText().trim();
        String position = position_field.getText().trim();
        String phone = phone_field.getText().trim();

        BigDecimal pay;
        int hours;

        // Validate pay and hours input is numeric and not negative, and that name and position are not empty.
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
            ? model.add(name, position, phone, pay, hours)
            : model.update(existing.id_employee(), name, position, phone, pay, hours);

        if (!ok) {
            show_warning("The employee could not be saved. Check the console for the database message.");
            return;
        }

        //reload the table in the main window and close this window
        on_added.run();                                   
        ((Node) submit_button).getScene().getWindow().hide();
    }

    /**
     * Confirms and deletes the employee currently being edited.
     */
    @FXML public void delete() {
        if (existing == null) {
            return;
        }

        Alert confirmation = new Alert(
            Alert.AlertType.CONFIRMATION,
            "Delete " + existing.name() + "? This action cannot be undone.",
            ButtonType.CANCEL,
            ButtonType.OK
        );
        confirmation.setTitle("Delete Employee");
        confirmation.setHeaderText(null);

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        if (!model.delete(existing.id_employee())) {
            show_warning("The employee could not be deleted. Check the console for the database message.");
            return;
        }

        on_added.run();
        ((Node) delete_button).getScene().getWindow().hide();
    }

    /**
     * Displays a warning message to the user.
     *
     * @param message the warning text to display
     */
    private void show_warning(String message) {
        new Alert(Alert.AlertType.WARNING, message).showAndWait();
    }

}
