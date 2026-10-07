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


    private static final int LOW_STOCK_THRESHOLD = 10;
    private static final boolean PROCESSING_FLAG_VALUE = true;

    @FXML private TextArea proc_orders_text_area;
    @FXML private TextArea today_oh_text_area;
    @FXML private Label TodaysSalesAmount;
    @FXML private Label TotalSalesAmount;
    @FXML private TableView<OrderHistory.OrderSummary> OHTable;
    @FXML private TableColumn<OrderHistory.OrderSummary, Integer> OHID;
    @FXML private TableColumn<OrderHistory.OrderSummary, String> OHOrder;
    @FXML private TableColumn<OrderHistory.OrderSummary, BigDecimal> OHTotal;
    @FXML private TextField OHSearch;
    @FXML private ComboBox<String> OHSortBy;
    @FXML private ComboBox<String> OHTimePeriod;

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

    @FXML public void initialize() {
        populate_inventory_areas();
        populate_order_areas();
    }

    private record InvLevel(String name, int amount) {}

    private static List<InvLevel> read_inventory() {
        List<InvLevel> levels = new ArrayList<>();
        List<List<?>> tables = List.of(
            PGComms.issue_query_type_rows_InvEdible("SELECT * FROM inv_edible ORDER BY 1"),
            PGComms.issue_query_type_rows_InvNonEdible("SELECT * FROM inv_nonedible ORDER BY 1"));
        for (List<?> rows : tables) {
            for (Object row : rows) {
                Object name = OrderHistory.field(row, 1);
                Object amount = OrderHistory.field(row, 2);
                levels.add(new InvLevel(String.valueOf(name), amount instanceof Integer i ? i : 0));
            }
        }
        return levels;
    }

    private void populate_inventory_areas() {
        if (out_of_stock_text_area == null && running_low_text_area == null) return;
        StringBuilder out = new StringBuilder();
        StringBuilder low = new StringBuilder();
        for (InvLevel item : read_inventory()) {
            String line = item.name() + "\t" + item.amount() + "\n";
            if (item.amount() <= 0) {
                out.append(line);
            } else if (item.amount() < LOW_STOCK_THRESHOLD) {
                low.append(line);
            }
        }
        if (out_of_stock_text_area != null) out_of_stock_text_area.setText(out.toString());
        if (running_low_text_area != null) running_low_text_area.setText(low.toString());
    }

    private void populate_order_areas() {
        if (proc_orders_text_area == null && today_oh_text_area == null
                && TodaysSalesAmount == null && TotalSalesAmount == null) return;

        StringBuilder processing = new StringBuilder();
        StringBuilder today = new StringBuilder();
        BigDecimal today_sales = BigDecimal.ZERO;
        BigDecimal total_sales = BigDecimal.ZERO;

        for (OrderHistory.OrderSummary o : OrderHistory.fetch_orders()) {
            String line = o.id() + "\t" + o.placed_text() + "\t$" + String.format("%.2f", o.total()) + "\n";
            total_sales = total_sales.add(o.total());
            if (o.is_today()) {
                today.append(line);
                today_sales = today_sales.add(o.total());
            }
            if (o.flag() == PROCESSING_FLAG_VALUE) {
                processing.append(line);
            }
        }

        if (proc_orders_text_area != null) proc_orders_text_area.setText(processing.toString());
        if (today_oh_text_area != null) today_oh_text_area.setText(today.toString());
        if (TodaysSalesAmount != null) TodaysSalesAmount.setText("$" + String.format("%.2f", today_sales));
        if (TotalSalesAmount != null) TotalSalesAmount.setText("$" + String.format("%.2f", total_sales));
    }

    public void initialize(OrderHistory oh) {
        this.order_history = oh;
        if (OHTable == null) return;
        if (OHID != null) {
            OHID.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().id()));
            OHID.setSortable(false);
        }
        if (OHOrder != null) {
            OHOrder.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().placed_text()));
            OHOrder.setSortable(false);
        }
        if (OHTotal != null) {
            OHTotal.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().total()));
            OHTotal.setSortable(false);
        }
        if (OHTimePeriod != null) {
            OHTimePeriod.setItems(FXCollections.observableArrayList("All time", "Today"));
            OHTimePeriod.setValue("All time");
        }
        if (OHSortBy != null) {
            OHSortBy.setItems(FXCollections.observableArrayList(
                "Newest first", "Oldest first", "Highest total", "Lowest total"));
            OHSortBy.setValue("Newest first");
        }
        oh.load_orders();
        OHTable.setItems(oh.get_orders());
    }

    @FXML void OHExitPressed(ActionEvent event) {
        if (order_history != null) order_history.close();
    }
    @FXML void OHSearchEnter(KeyEvent event) {
        if (order_history != null && OHSearch != null) order_history.filter(OHSearch.getText());
    }
    @FXML void OHSortByPick(ActionEvent event) {
        if (order_history != null && OHSortBy != null) order_history.sort_by(OHSortBy.getValue());
    }
    @FXML void OHTimePeriodPick(ActionEvent event) {
        if (order_history != null && OHTimePeriod != null) order_history.set_period(OHTimePeriod.getValue());
    }
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