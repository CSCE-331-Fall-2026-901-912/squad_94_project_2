package controller;

import model.ItemDetailsPopUp;
import model.AddItemPopUp;
import model.InventoryScreen;
import model.MainScreenGUI;
import model.OrderHistory;
import model.SalesAnalytics;

import dto.InvEdibleRowDTO;
import dto.InvNonEdibleRowDTO;
import dto.EmployeesRowDTO;
import dto.MenuDrinksRowDTO;
import dto.MenuToppingsRowDTO;
import dto.JoinMenuDrinksAndInvEdibleRowDTO;
import dto.JoinMenuToppingsAndInvEdibleRowDTO;
import dto.OrdersRowDTO;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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




public class ManagerController {
    
    private InventoryScreen inventory_screen;
    private MainScreenGUI main_screen_gui;
    private OrderHistory order_history;
    private SalesAnalytics sales_analytics;

//    //INVENTORYSCREEN
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


    @FXML
    private AddItemPopUp add_item_pop_up;;
    @FXML
    public void add_item_click() {
        add_item_pop_up.add_item_click();
    }





    public void initialize(InventoryScreen inventory_screen){
        this.inventory_screen = inventory_screen;

    }

    public void initialize(ItemDetailsPopUp item_details_pop_up){
        this.item_details_pop_up = item_details_pop_up;

    }

    public void initialize(AddItemPopUp add_item_pop_up){
        this.add_item_pop_up = add_item_pop_up;
        
    }

//
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
//    // MAIN SCREEN
//    @FXML
//    private AnchorPane InventoryBox;
//
//    @FXML
//    private Label InventoryLabel;
//
//    @FXML
//    private Label ManagerLabel;
//
//    @FXML
//    private TableColumn<?, ?> OOSAmount;
//
//    @FXML
//    private Label OOSLabel;
//
//    @FXML
//    private TableColumn<?, ?> OOSName;
//
//    @FXML
//    private TableView<?> OOSTable;
//
//    @FXML
//    private AnchorPane ProcOrderBox;
//
//    @FXML
//    private TableColumn<?, ?> ProcOrderID;
//
//    @FXML
//    private TableView<?> ProcOrderTable;
//
//    @FXML
//    private TableColumn<?, ?> ProcOrderTotal;
//
//    @FXML
//    private TableColumn<?, ?> ProcOrders;
//
//    @FXML
//    private TableColumn<?, ?> RLAmount;
//
//    @FXML
//    private TableColumn<?, ?> RLName;
//
//    @FXML
//    private TableView<?> RLTable;
//
//    @FXML
//    private AnchorPane SalesBox;
//
//    @FXML
//    private Label SalesLabel;
//
//    @FXML
//    private AnchorPane TodayOHBox;
//
//    @FXML
//    private TableColumn<?, ?> TodayOHID;
//
//    @FXML
//    private TableColumn<?, ?> TodayOHOrder;
//
//    @FXML
//    private TableView<?> TodayOHTable;
//
//    @FXML
//    private TableColumn<?, ?> TodayOHTotal;
//
//    @FXML
//    private SplitPane TodaysSales;
//
//    @FXML
//    private Label TodaysSalesAmount;
//
//    @FXML
//    private Label TodaysSalesLabel;
//
//    @FXML
//    private SplitPane TotalSales;
//
//    @FXML
//    private Label TotalSalesAmount;
//
//    @FXML
//    private Label TotalSalesLabel;
//
//    @FXML
//    void InventoryOpen(MouseEvent event) {
//
//    }
//
//    @FXML
//    void OHOpen(MouseEvent event) {
//
//    }
//
//    @FXML
//    void ProcOrdersOpen(MouseEvent event) {
//
//    }
//
//    @FXML
//    void SalesEntered(MouseEvent event) {
//
//    }
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
