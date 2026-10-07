package model;

import controller.ManagerController;
import database.PGComms;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.time.*;
import database.PGComms.*;

public class MainScreenGUI {
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
}
