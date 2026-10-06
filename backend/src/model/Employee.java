package model;

import database.PGComms;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import java.math.BigDecimal;
import dto.EmployeesRowDTO;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Employee {

    @FXML private TableView<EmployeesRowDTO> employee_table;
    @FXML private TableColumn<EmployeesRowDTO, Integer>    emp_id;
    @FXML private TableColumn<EmployeesRowDTO, String>     emp_name;
    @FXML private TableColumn<EmployeesRowDTO, String>     emp_pos;
    @FXML private TableColumn<EmployeesRowDTO, String>     emp_phone;
    @FXML private TableColumn<EmployeesRowDTO, BigDecimal> emp_pay;
    @FXML private TableColumn<EmployeesRowDTO, Integer>    emp_hours;

    public void load_employees(){
        emp_id.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().id_employee()));
        emp_name.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().name()));
        emp_pos.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().position()));
        emp_phone.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().phone_number()));
        emp_pay.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().current_pay_rate()));
        emp_hours.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().hours_worked_for_week()));
        setup_row_click();
        
        employee_table.setItems(FXCollections.observableArrayList(
        PGComms.issue_query_type_rows_Employees(
            "SELECT id_employee, name, position, phone_number, current_pay_rate, hours_worked_for_week "
        + "FROM employees ORDER BY id_employee")));
    }
    
    @FXML public void open_add_employee(ActionEvent event){
        show_employee_form(((Node) event.getSource()).getScene().getWindow(), null);
    }

    private void show_employee_form(Window owner, EmployeesRowDTO existing){
        try {
            java.net.URL url = getClass().getResource("/gui/manager/AddNewEmployee.fxml");
            if (url == null) {
                System.out.println("AddNewEmployee.fxml not found");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(new AddEmployee(this::load_employees, existing));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(existing == null ? "Add New Employee" : "Edit Employee");
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Set up double-click on a row to open the edit form
    private void setup_row_click(){
        employee_table.setRowFactory(tv -> {
            TableRow <EmployeesRowDTO> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {          // double-click on a real row
                    show_employee_form(tv.getScene().getWindow(), row.getItem());
                }
            });
            return row;
        });
    }
}