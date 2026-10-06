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
import javafx.scene.control.TableView;
import java.math.BigDecimal;
import dto.EmployeesRowDTO;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Employee {

    @FXML private TableView<EmployeesRowDTO> EmployeeTable;
    @FXML private TableColumn<EmployeesRowDTO, Integer>    EmpID;
    @FXML private TableColumn<EmployeesRowDTO, String>     EmpName;
    @FXML private TableColumn<EmployeesRowDTO, String>     EmpPos;
    @FXML private TableColumn<EmployeesRowDTO, String>     EmpPhone;
    @FXML private TableColumn<EmployeesRowDTO, BigDecimal> EmpPay;
    @FXML private TableColumn<EmployeesRowDTO, Integer>    EmpHours;

    public void load_employees(){
        EmpID.setCellValueFactory(d    -> new ReadOnlyObjectWrapper<>(d.getValue().id_employee()));
        EmpName.setCellValueFactory(d  -> new ReadOnlyObjectWrapper<>(d.getValue().name()));
        EmpPos.setCellValueFactory(d   -> new ReadOnlyObjectWrapper<>(d.getValue().position()));
        EmpPhone.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().phone_number()));
        EmpPay.setCellValueFactory(d   -> new ReadOnlyObjectWrapper<>(d.getValue().current_pay_rate()));
        EmpHours.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().hours_worked_for_week()));

        EmployeeTable.setItems(FXCollections.observableArrayList(PGComms.get_employees()));
    }
    
    @FXML public void open_add_employee(ActionEvent event){
        try {
            java.net.URL url = getClass().getResource("/gui/manager/AddNewEmployee.fxml");   // use your file name
            if (url == null) {
                System.out.println("AddNewEmployee.fxml not found");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(new AddEmployee(this::load_employees));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add New Employee");
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);       // blocks the employee window until this closes
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}