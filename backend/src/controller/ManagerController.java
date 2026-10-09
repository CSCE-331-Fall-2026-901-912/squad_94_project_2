package controller;

import dto.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import model.EmployeeModel;
import model.InventoryModel;
import model.MenuModel;
import model.OrderHistoryModel;
import model.SalesModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;




public class ManagerController {

    @FXML private TableView<IDNameValDTO> OOSTable;
    @FXML private TableColumn<IDNameValDTO, String> OOSName;
    @FXML private TableColumn<IDNameValDTO, Integer> OOSAmount;

    @FXML private TableView<IDNameValDTO> RLTable;
    @FXML private TableColumn<IDNameValDTO, String> RLName;
    @FXML private TableColumn<IDNameValDTO, Integer> RLAmount;

    @FXML private Label TodaysSalesAmount;
    @FXML private Label TotalSalesAmount;

    private SalesModel sales_model = new SalesModel();
    private InventoryModel inventory_model = new InventoryModel();

    @FXML public void initialize() {
        refresh();
    }

    @FXML 
    public void open_employee_view(ActionEvent event){
        try{
            EmployeeModel model = new EmployeeModel();
            EmployeeController controller =
                new EmployeeController(model);

            Stage stage = ViewLoader.open_window(
                "/gui/manager/EmployeeView.fxml",
                controller,
                ((Node) event.getSource()).getScene().getWindow());

            if (stage != null) {
                stage.setTitle("Employees");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void open_menu_view(ActionEvent event){
        try {
            MenuModel model = new MenuModel();
            MenuController controller = new MenuController(model);

            Stage stage = ViewLoader.open_window(
                "/gui/manager/MenuView.fxml",
                controller,
                ((Node) event.getSource()).getScene().getWindow());
            
            if (stage != null) {
                stage.setTitle("Menu");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void open_edit_inventory(ActionEvent event) {
        try {
            InventoryModel model = new InventoryModel();
            InventoryController controller =
                new InventoryController(model);

            Stage stage = ViewLoader.open_window(
                "/gui/manager/InventoryScreen.fxml",
                controller,
                ((Node) event.getSource()).getScene().getWindow());

            if (stage != null) {
                stage.setTitle("Inventory");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML public void open_order_history(MouseEvent event) {
        try {
            OrderHistoryModel model = new OrderHistoryModel();
            OrderHistoryController controller = new OrderHistoryController(model);
            
            Stage stage = ViewLoader.open_window(
                "/gui/manager/OrderHistory.fxml",
                controller,
                ((Node) event.getSource()).getScene().getWindow());
            
            if (stage != null) {
                stage.setTitle("Order History");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML private void open_sales_analytics(MouseEvent event){
        try {
           // TODO: fill in once sales analytics is implemented
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh(){
        TodaysSalesAmount.setText("$" + sales_model.get_todays_sales().toString());
        TotalSalesAmount.setText("$" + sales_model.get_total_sales().toString());
        load_inventory_details_small();
    }

    public void load_inventory_details_small() {
        // Load out-of-stock table
        OOSName.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().name()));
        OOSAmount.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().val()));
        OOSTable.setItems(FXCollections.observableArrayList(inventory_model.get_out_of_stock()));

        // Load running low on table
        RLName.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().name()));
        RLAmount.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().val()));
        RLTable.setItems(FXCollections.observableArrayList(inventory_model.get_running_low()));
    }
}