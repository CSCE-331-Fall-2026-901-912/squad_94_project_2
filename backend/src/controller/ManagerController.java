package controller;

import model.ItemDetailsPopUp;
import model.InventoryScreen;
import model.MainScreenGUI;
import model.OrderHistory;
import model.SalesAnalytics;
import model.Employee;
import model.ProcessingOrders;
import database.PGComms;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

import dto.InvEdibleRowDTO;
import dto.InvNonEdibleRowDTO;
import dto.EmployeesRowDTO;
import dto.MenuDrinksRowDTO;
import dto.MenuToppingsRowDTO;
import dto.JoinMenuDrinksAndInvEdibleRowDTO;
import dto.JoinMenuToppingsAndInvEdibleRowDTO;
import dto.OrdersRowDTO;
import javafx.scene.input.MouseEvent;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;


public class ManagerController {
    
    private MainScreenGUI main_screen_gui = new MainScreenGUI();
    private InventoryScreen inventory_screen;
    private OrderHistory order_history;
    private SalesAnalytics sales_analytics;
    private ProcessingOrders proc_orders;

    //INVENTORYSCREEN
    @FXML
    private Button edible_button;

    @FXML
    private Button inedible_button;

    @FXML
    private GridPane inventory_grid;

    @FXML
    private TextField inventory_search_bar;

    @FXML
    private TextArea out_of_stock_text_area;

    @FXML
    private TextArea running_low_text_area;

    @FXML
    void filter_edible() {

    }

    @FXML
    void filter_inedible() {

    }

    private ItemDetailsPopUp item_details_pop_up;

    @FXML
    void item_click(ActionEvent event) {
        Button but = (Button) event.getSource();
        item_details_pop_up = inventory_screen.item_click(this, but.getText());
    }

    @FXML public void ProcOrdersOpen(MouseEvent event){
        // TODO: open the processing orders view
        proc_orders = main_screen_gui.open_proc_orders(this);
    }
    @FXML public void OHOpen(MouseEvent event){
        // TODO: open today's order history
        order_history = main_screen_gui.open_order_history(this);
    }
    @FXML public void InventoryOpen(MouseEvent event){
        // TODO: open inventory
        inventory_screen = main_screen_gui.open_inventory(this);
    }
    @FXML public void SalesEntered(MouseEvent event){
        // TODO: open sales
        sales_analytics = main_screen_gui.open_sales_analytics(this);
    }

    @FXML void OHExitPressed(ActionEvent event) {
        if (order_history != null) order_history.close();
    }
    public void initialize(OrderHistory oh) { this.order_history = oh; }
    public void initialize(SalesAnalytics sales) { this.sales_analytics = sales; }
    public void initialize(ProcessingOrders proc) { this.proc_orders = proc; }
    public void initialize(InventoryScreen inv) { this.inventory_screen = inv; }
    
    @FXML 
    public void open_employee_view(ActionEvent event){
        try {
            java.net.URL url = getClass().getResource("/gui/manager/EmployeeView.fxml");
            if (url == null) {
                System.out.println("EmployeeView.fxml not found");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Employee employee = new Employee();
            loader.setController(employee);

            Parent root = loader.load();
            employee.load_employees();

            Stage stage = new Stage();
            stage.setTitle("Employees");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}