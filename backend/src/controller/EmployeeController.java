package controller;

import dto.EmployeesRowDTO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Window;
import javafx.stage.Stage;
import model.EmployeeModel;

import java.math.BigDecimal;

/**
 * Controller for viewing the employee table and managing the add employee form's opening 
 *
 * @author Squad 94 - Sanjana Ram
 */
public class EmployeeController {
    private final EmployeeModel model;

    @FXML private TableView<EmployeesRowDTO> employee_table;
    @FXML private TableColumn<EmployeesRowDTO, Integer>    emp_id;
    @FXML private TableColumn<EmployeesRowDTO, String>     emp_name;
    @FXML private TableColumn<EmployeesRowDTO, String>     emp_pos;
    @FXML private TableColumn<EmployeesRowDTO, String>     emp_phone;
    @FXML private TableColumn<EmployeesRowDTO, BigDecimal> emp_pay;
    @FXML private TableColumn<EmployeesRowDTO, Integer>    emp_hours;

    public EmployeeController(EmployeeModel model) {
        this.model = model;
    }

    /**
     * Initialize the table with employee data and set up double-click to edit functionality.
     */
    @FXML 
    private void initialize() {
        emp_id.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().id_employee()));
        emp_name.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().name()));
        emp_pos.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().position()));
        emp_phone.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().phone_number()));
        emp_pay.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().current_pay_rate()));
        emp_hours.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().hours_worked_for_week()));

        setup_row_click();
        refresh_table();
    }

    /**
     * Ensures the table is up to date with the latest employee data from the model.
     * This method is called after an employee is added, edited, or deleted.
     */
    public void refresh_table() {
        employee_table.setItems(FXCollections.observableArrayList(model.get_all()));
    }

    /**
     * Open the AddEmployeeController form for adding a new employee 
     * and set the owner to the current window to prevent multiple windows from being opened at once.
     * @param event the action event triggered by the button click
     */
    @FXML
    public void open_add_employee(ActionEvent event) {
        Window owner = ((Node) event.getSource()).getScene().getWindow();
        open_employee_form(owner, null);
    }

    /**
     * General open add controller method that opens the add employee form for adding or editing an employee.
     * @param owner the window that owns the form to prevent multiple windows from being opened at once
     * @param existing the employee being edited, or {@code null} when adding an employee
     */
    private void open_employee_form(Window owner, EmployeesRowDTO existing) {
        AddEmployeeController controller = new AddEmployeeController(model, this::refresh_table , existing);
        Stage stage = ViewLoader.open_window(
            "/gui/manager/AddNewEmployee.fxml",
            controller,
            owner);

        if (stage != null) {
            stage.setTitle(
                existing == null ? "Add New Employee" : "Edit Employee");
        }
    }

    /**
     * Set up double-click functionality (to prevent accidental openning) on table rows to open the employee form for editing.
     */
    private void setup_row_click() {
        employee_table.setRowFactory(table -> {
            TableRow<EmployeesRowDTO> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    open_employee_form(table.getScene().getWindow(), row.getItem());
                }
            });

            return row;
        });
    }
}
