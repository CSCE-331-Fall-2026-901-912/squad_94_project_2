package controller;

import model.ItemDetailsPopUp;
import model.InventoryScreen;
import model.MainScreenGUI;
import model.OrderHistory;
import model.SalesAnalytics;


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



public class ManagerController {
    private ItemDetailsPopUp item_details_pop_up;
    private InventoryScreen inventory_screen;
    private MainScreenGUI main_screen_gui;
    private OrderHistory order_history;
    private SalesAnalytics sales_analytics;

    //INVENTORYSCREEN
    @FXML
    private TableColumn<Item, Integer> amountOutOfStockCol;

    @FXML
    private TableColumn<Item, Integer> amountRunningLowCol;

    @FXML
    private Button edibleButton;

    @FXML
    private Button inedibleButton;

    @FXML
    private GridPane inventoryGrid;

    @FXML
    private TextField inventorySearchBar;

    @FXML
    private TableColumn<Item, String> nameOutOfStockCol;

    @FXML
    private TableColumn<Item, String> nameRunningLowCol;

    @FXML
    private TableView<Item> outOfStockTable;

    @FXML
    private TableView<Item> runningLowTable;

    //ITEMDETAILSPOPUP
    @FXML
    private TextField inputNumberTextField;

    @FXML
    private ImageView itemImage;

    //PROCESSINGORDERS
    @FXML
    private TableColumn<Order, Integer> idProcessingOrdersCol;

    @FXML
    private TableColumn<Order, String> orderProcessingOrdersCol;

    @FXML
    private TextField searchBarProcessingOrders;

    @FXML
    private ComboBox<String> sortByDropdownProcessingOrders;

    @FXML
    private TableView<Order> tableProcessingOrders;

    @FXML
    private ComboBox<String> timePeriodDropdownProcessingOrders;

    @FXML
    private TableColumn<Order, Double> totalProcessingOrdersCol;


    //INVENTORYSCREEN FUNCTIONS
    @FXML
    void filterEdible(ActionEvent event) {

    }

    @FXML
    void filterInedible(ActionEvent event) {

    }

    @FXML
    void itemClick(ActionEvent event) {

    }



    // MAIN SCREEN
    @FXML
    private AnchorPane InventoryBox;

    @FXML
    private Label InventoryLabel;

    @FXML
    private Label ManagerLabel;

    @FXML
    private TableColumn<?, ?> OOSAmount;

    @FXML
    private Label OOSLabel;

    @FXML
    private TableColumn<?, ?> OOSName;

    @FXML
    private TableView<?> OOSTable;

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
    private TableColumn<?, ?> RLAmount;

    @FXML
    private TableColumn<?, ?> RLName;

    @FXML
    private TableView<?> RLTable;

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
    void InventoryOpen(MouseEvent event) {

    }

    @FXML
    void OHOpen(MouseEvent event) {

    }

    @FXML
    void ProcOrdersOpen(MouseEvent event) {

    }

    @FXML
    void SalesEntered(MouseEvent event) {

    }
}
