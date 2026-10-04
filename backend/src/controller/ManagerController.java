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

}
