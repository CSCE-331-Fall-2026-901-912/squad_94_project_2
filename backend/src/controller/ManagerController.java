package controller;

import dto.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import model.ItemDetailsPopUp;
import model.InventoryScreen;
import model.MainScreenGUI;
import model.OrderHistory;
import model.SalesAnalytics;
import model.Employee;
import model.Menu;

import java.net.IDN;
import java.util.ArrayList;

import database.PGComms;
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
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.time.*;



public class ManagerController {
    
    private InventoryScreen inventory_screen;
    private MainScreenGUI main_screen_gui;
    private OrderHistory order_history;
    private SalesAnalytics sales_analytics;



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

    // MAIN SCREEN
    @FXML
    private AnchorPane InventoryBox;

    @FXML
    private Label InventoryLabel;

    @FXML
    private Label ManagerLabel;

    @FXML
    private Label OOSLabel;

    @FXML
    private TableView<IDNameValDTO> OOSTable;

    @FXML
    private TableColumn<IDNameValDTO, String> OOSName;

    @FXML
    private TableColumn<IDNameValDTO, Integer> OOSAmount;

    @FXML
    private AnchorPane ProcOrderBox;

    @FXML
    private TableColumn<?, ?> ProcOrderID;

    @FXML
    private TableView<?> ProcOrderTable;

    @FXML
    private TableColumn<?, ?> ProcOrderTotal;

    @FXML
    private TableColumn<?, ?> ProcOrders;

    @FXML
    private TableView<IDNameValDTO> RLTable;

    @FXML
    private TableColumn<IDNameValDTO, String> RLName;

    @FXML
    private TableColumn<IDNameValDTO, Integer> RLAmount;

    @FXML
    private AnchorPane SalesBox;

    @FXML
    private Label SalesLabel;

    @FXML
    private AnchorPane TodayOHBox;

    @FXML
    private TableColumn<?, ?> TodayOHID;

    @FXML
    private TableColumn<?, ?> TodayOHOrder;

    @FXML
    private TableView<?> TodayOHTable;

    @FXML
    private TableColumn<?, ?> TodayOHTotal;

    @FXML
    private SplitPane TodaysSales;

    @FXML
    private Label TodaysSalesAmount;

    @FXML
    private Label TodaysSalesLabel;

    @FXML
    private SplitPane TotalSales;

    @FXML
    private Label TotalSalesAmount;

    @FXML
    private Label TotalSalesLabel;

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
    }
    @FXML public void OHOpen(MouseEvent event){
        // TODO: open today's order history
    }
    @FXML public void InventoryOpen(MouseEvent event){
        // TODO: open inventory
    }
    @FXML public void SalesEntered(MouseEvent event){
        // TODO: open sales
    }
    
    @FXML 
    public void open_employee_view(ActionEvent event){
        try {
            java.net.URL url = getClass().getResource("/gui/manager/EmployeeView.fxml");
            if (url == null) {                                   // avoids the "Location is not set" error
                System.out.println("EmployeeView.fxml not found");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Employee employee = new Employee();
            loader.setController(employee);

            Parent root = loader.load();
            employee.load_employees();                // fill the table after the FXML loads

            Stage stage = new Stage();
            stage.setTitle("Employees");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML 
    public void open_menu_view(ActionEvent event){
        try {
            java.net.URL url = getClass().getResource("/gui/manager/MenuView.fxml");
            if (url == null) {                                   // avoids the "Location is not set" error
                System.out.println("MenuView.fxml not found");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Menu menu = new Menu();
            loader.setController(menu);

            Parent root = loader.load();
            menu.load_menu();                // fill the table after the FXML loads

            Stage stage = new Stage();
            stage.setTitle("Menu");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get_todays_sales() {
        return PGComms.get_sales_total_date(LocalDate.now().toString()).toString();
    }

    public String get_sales_total() {
        return PGComms.get_sales_total().toString();
    }

    public void populate_processing_orders() {

    }

    public void get_OOS() {

    }

    public void initialize(){
        TodaysSalesAmount.setText("$" + get_todays_sales());
        TotalSalesAmount.setText("$" + get_sales_total());
        load_inventory_details_small();
    }

    public void load_inventory_details_small() {

        // Load out-of-stock table
        OOSName.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().name()));
        OOSAmount.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().val()));
        OOSTable.setItems(FXCollections.observableArrayList(PGComms.get_out_of_stock()));

        // Load running low on table
        RLName.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().name()));
        RLAmount.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().val()));
        RLTable.setItems(FXCollections.observableArrayList(PGComms.get_running_low_on()));
    }

//    //ITEMDETAILSPOPUP
//    @FXML
//    private TextField inputNumberTextField;
//
//    @FXML
//    private ImageView itemImage;
//
//    //PROCESSINGORDERS
//    @FXML
//    private TableColumn<Order, Integer> idProcessingOrdersCol;
//
//    @FXML
//    private TableColumn<Order, String> orderProcessingOrdersCol;
//
//    @FXML
//    private TextField searchBarProcessingOrders;
//
//    @FXML
//    private ComboBox<String> sortByDropdownProcessingOrders;
//
//    @FXML
//    private TableView<Order> tableProcessingOrders;
//
//    @FXML
//    private ComboBox<String> timePeriodDropdownProcessingOrders;
//
//    @FXML
//    private TableColumn<Order, Double> totalProcessingOrdersCol;
//
//
//    //INVENTORYSCREEN FUNCTIONS
//    @FXML
//    void filterEdible(ActionEvent event) {
//
//    }
//
//    @FXML
//    void filterInedible(ActionEvent event) {
//
//    }
//
//    @FXML
//    void itemClick(ActionEvent event) {
//
//    }
//
//
//


//    // ORDER HISTORY
//    @FXML
//    private Button OHExit;
//
//    @FXML
//    private TableColumn<?, ?> OHID;
//
//    @FXML
//    private Label OHLabel;
//
//    @FXML
//    private TableColumn<?, ?> OHOrder;
//
//    @FXML
//    private TextField OHSearch;
//
//    @FXML
//    private ComboBox<?> OHSortBy;
//
//    @FXML
//    private TableView<?> OHTable;
//
//    @FXML
//    private ComboBox<?> OHTimePeriod;
//
//    @FXML
//    private TableColumn<?, ?> OHTotal;
//
//    @FXML
//    void OHExitPressed(ActionEvent event) {
//
//    }
//
//    @FXML
//    void OHSearchEnter(KeyEvent event) {
//
//    }
//
//    @FXML
//    void OHSortByPick(ActionEvent event) {
//
//    }
//
//    @FXML
//    void OHTimePeriodPick(ActionEvent event) {
//
//    }
}
