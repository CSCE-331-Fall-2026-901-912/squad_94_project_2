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

    public void refresh_table() {
        employee_table.setItems(FXCollections.observableArrayList(model.get_all()));
    }

    @FXML
    public void open_add_employee(ActionEvent event) {
        Window owner = ((Node) event.getSource()).getScene().getWindow();
        open_employee_form(owner, null);
    }
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
