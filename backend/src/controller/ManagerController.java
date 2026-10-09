package controller;

import dto.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import model.AddItemPopUp;
import model.MainScreenGUI;
import model.OrderHistory;
import model.SalesAnalytics;
import model.EmployeeModel;
import model.InventoryModel;
import model.ProcessingOrders;
import model.MenuModel;
import database.PGComms;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextArea;
import java.time.*;



public class ManagerController {
    
    private MainScreenGUI main_screen_gui = new MainScreenGUI();
    private OrderHistory order_history;
    private SalesAnalytics sales_analytics;
    private ProcessingOrders proc_orders;

    //INVENTORYSCREEN
    private AddItemPopUp add_item_pop_up;

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
    private Button InventoryLabel;

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

    // private ItemDetailsPopUp item_details_pop_up;
    // @FXML
    // void item_click(ActionEvent event) {
    //     Button but = (Button) event.getSource();
    //     item_details_pop_up = inventory_screen.item_click(this, but.getText());
    // }


    // @FXML
    // private AddItemPopUp add_item_pop_up;
    // @FXML
    // public void add_item_click() {
    //     add_item_pop_up.add_item_click();
    // }




    @FXML private TextArea non_edible_text_area;
    @FXML private TextArea edible_text_area;

    @FXML private TextField input_name;
    @FXML private TextField input_quantity;

    public void initialize(AddItemPopUp add_item_pop_up){
        this.add_item_pop_up = add_item_pop_up;



        // input_name.textProperty().bind(add_item_pop_up.get_input_name()); //with bind updates every time a change is made
        // input_quantity.textProperty().bind(add_item_pop_up.get_input_quantity());



        // input_quantity.setEditable(true);
        // input_name.setEditable(true);

        //find_and_change_item();

    }



    // public void initialize(ItemDetailsPopUp item_details_pop_up){
    //     this.item_details_pop_up = item_details_pop_up;

    // }

    // public void initialize(AddItemPopUp add_item_pop_up){
    //     this.add_item_pop_up = add_item_pop_up;

    // }

//
//    @FXML public void ProcOrdersOpen(MouseEvent event){
//        // TODO: open the processing orders view
//        proc_orders = main_screen_gui.open_proc_orders(this);
//    }
    @FXML public void OHOpen(MouseEvent event){
        // TODO: open today's order history
        order_history = main_screen_gui.open_order_history(this);
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

    @FXML private void open_edit_inventory(ActionEvent event) {
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
