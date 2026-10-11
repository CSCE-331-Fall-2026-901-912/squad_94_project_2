package controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

import database.PGComms;
import dto.JoinMenuDrinksAndInvEdibleRowDTO;
import dto.MenuDrinksRowDTO;
import dto.OrdersRowDTO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableRow;
import model.OrderHistoryModel;

public class ProdSalesScreenController {


    // FXML Declarations

    @FXML private TextField from_year;
    @FXML private TextField from_month;
    @FXML private TextField from_day;

    @FXML private TextField to_year;
    @FXML private TextField to_month;
    @FXML private TextField to_day;

    private record SalesRecord(Integer id_drink, String name_drink, Double total_sales_drink) {}

    @FXML private TableView<SalesRecord> sales_table;
    @FXML private TableColumn<SalesRecord, Integer> sales_id_column;
    @FXML private TableColumn<SalesRecord, String> sales_name_column;
    @FXML private TableColumn<SalesRecord, Double> total_sales_column;

    // Data Structures for Sales Records and Filtering
    private FilteredList<SalesRecord> filtered_sales;
    private SortedList<SalesRecord> sorted_sales;
    private HashMap<Integer, Double> id_to_total_sales = new HashMap<>();

    // All orders fetched from the database
    ObservableList<OrdersRowDTO> all_orders;

    // Model for accessing order history and related data
    private OrderHistoryModel model;
    
    @FXML public void sort(){
        // Grab the start and end times from the text fields
        OffsetDateTime start = OffsetDateTime.of(
            Integer.parseInt(from_year.getText()),
            Integer.parseInt(from_month.getText()),
            Integer.parseInt(from_day.getText()),
            0, 0, 0, 0,
            ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now())
        );

        OffsetDateTime end = OffsetDateTime.of(
            Integer.parseInt(to_year.getText()),
            Integer.parseInt(to_month.getText()),
            Integer.parseInt(to_day.getText()),
            23, 59, 59, 999_999_999,
            ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now())
        );

        // Sort both tables based on the specified time range
        sort_sales_table_by_time(start, end);
        sort_prods_table_by_time(start, end);
    }

    // Controller constructor for syncing the model with this controller
    public ProdSalesScreenController(OrderHistoryModel model) {
        this.model = model;
    }
    
    @FXML public void initialize() {
        sales_setup();
        prods_setup();
    }

    private void sort_sales_table_by_time(OffsetDateTime start, OffsetDateTime end){
        
        id_to_total_sales = new HashMap<>();

        // Get all orders within the specified time range
        for (OrdersRowDTO order : all_orders) {
            if (model.in_period(order,start,end)){
                // Add together the total sales for each drink
                id_to_total_sales.put(order.id_drink(), id_to_total_sales.getOrDefault(order.id_drink(), 0.0) + order.total_spent().doubleValue());
            }
        }

        ObservableList<SalesRecord> sale_orders = FXCollections.observableArrayList();

        // Add the total sales for each drink to the observable list
        for (Integer id_drink : id_to_total_sales.keySet()) {
            sale_orders.add(new SalesRecord(
                id_drink,
                model.drink_name(id_drink),
                id_to_total_sales.get(id_drink)
            ));
        }

        // Wrap the observable list in a filtered and sorted list for the table view
        filtered_sales = new FilteredList<>(sale_orders);
        sorted_sales = new SortedList<>(filtered_sales);
        sales_table.setItems(sorted_sales);
        sorted_sales.comparatorProperty().bind(sales_table.comparatorProperty());
    }


    private void sales_setup(){
        model.load_drink_names();

        sales_id_column.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().id_drink()));
        sales_name_column.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().name_drink()));
        total_sales_column.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().total_sales_drink()));
        sales_setup_row_click();

        all_orders = FXCollections.observableArrayList(PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order"));

        for (OrdersRowDTO order : all_orders) {
            id_to_total_sales.put(order.id_drink(), id_to_total_sales.getOrDefault(order.id_drink(), 0.0) + order.total_spent().doubleValue());
        }


        ObservableList<SalesRecord> sale_orders = FXCollections.observableArrayList();

        for (Integer id_drink : id_to_total_sales.keySet()) {
            sale_orders.add(new SalesRecord(
                id_drink,
                model.drink_name(id_drink),
                id_to_total_sales.get(id_drink)
            ));
        }


        filtered_sales = new FilteredList<>(sale_orders);
        sorted_sales = new SortedList<>(filtered_sales);

        sales_table.setItems(sorted_sales);

        sorted_sales.comparatorProperty().bind(sales_table.comparatorProperty());
    }

    // Set up row click behavior for the sales table
    private void sales_setup_row_click(){
        sales_table.setRowFactory(tv -> {
            TableRow <SalesRecord> row = new TableRow<>();
            return row;
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    // PROD USAGE
    /////////////////////////////////////////////////////////////////////////////////////////////

    // FXML declarations
    private record ProdUseRecord(Integer id_edible, String name_edible, Integer inventory_used) {}

    @FXML private TableView<ProdUseRecord> prod_table;
    @FXML private TableColumn<ProdUseRecord, Integer> prod_id_column;
    @FXML private TableColumn<ProdUseRecord, String> prod_name_column;
    @FXML private TableColumn<ProdUseRecord, Integer> total_prod_column;

    // Data structures for formatting the table view
    private FilteredList<ProdUseRecord> filtered_prods;
    private SortedList<ProdUseRecord> sorted_prods;
    private HashMap<Integer, Integer> edible_id_to_prod = new HashMap<>();

    private void sort_prods_table_by_time(OffsetDateTime start, OffsetDateTime end){

        ArrayList<JoinMenuDrinksAndInvEdibleRowDTO> id_drink_to_id_edible = PGComms.issue_query_type_rows_JoinMenuDrinksAndInvEdible("SELECT * FROM join_menu_drinks_and_inv_edible");

        edible_id_to_prod = new HashMap<>();

        // Get all orders within the specified time range
        for (OrdersRowDTO order : all_orders) {
            if (model.in_period(order,start,end)){
                
                // Iterate through the join table to find all edibles used in this order
                for(JoinMenuDrinksAndInvEdibleRowDTO row : id_drink_to_id_edible){
                    if(row.id_drink() == order.id_drink()){
                        edible_id_to_prod.put(row.id_edible(), edible_id_to_prod.getOrDefault(row.id_edible(), 0) + 1);
                    }
                }
            }
        }

        ObservableList<ProdUseRecord> prod_orders = FXCollections.observableArrayList();

        for (Integer id_edible : edible_id_to_prod.keySet()) {
            prod_orders.add(new ProdUseRecord(
                id_edible,
                model.edible_name(id_edible),
                edible_id_to_prod.get(id_edible)
            ));
        }


        // Wrap the observable list in a filtered and sorted list for the table view
        filtered_prods = new FilteredList<>(prod_orders);
        sorted_prods = new SortedList<>(filtered_prods);
        prod_table.setItems(sorted_prods);
        sorted_prods.comparatorProperty().bind(prod_table.comparatorProperty());
    }

    private void prods_setup(){
        model.load_edible_names();

        prod_id_column.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().id_edible()));
        prod_name_column.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().name_edible()));
        total_prod_column.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().inventory_used()));
        prod_setup_row_click();

        all_orders = FXCollections.observableArrayList(PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order"));

        ArrayList<JoinMenuDrinksAndInvEdibleRowDTO> id_drink_to_id_edible = PGComms.issue_query_type_rows_JoinMenuDrinksAndInvEdible("SELECT * FROM join_menu_drinks_and_inv_edible");

        
        // Get all orders
        for (OrdersRowDTO order : all_orders) {
            // Iterate through the join table to find all edibles used in this order
            for(JoinMenuDrinksAndInvEdibleRowDTO row : id_drink_to_id_edible){
                if(row.id_drink() == order.id_drink()){
                    edible_id_to_prod.put(row.id_edible(), edible_id_to_prod.getOrDefault(row.id_edible(), 0) + 1);
                }
            }
        }



        ObservableList<ProdUseRecord> prod_orders = FXCollections.observableArrayList();

        // Add each edible with usage count to the observable list
        for (Integer id_edible : edible_id_to_prod.keySet()) {
            prod_orders.add(new ProdUseRecord(
                id_edible,
                model.edible_name(id_edible),
                edible_id_to_prod.get(id_edible)
            ));
        }

        // Wrap the observable list in a filtered and sorted list for the table view
        filtered_prods = new FilteredList<>(prod_orders);
        sorted_prods = new SortedList<>(filtered_prods);
        prod_table.setItems(sorted_prods);
        sorted_prods.comparatorProperty().bind(prod_table.comparatorProperty());
    }

    // Set up row click behavior for the product table
    private void prod_setup_row_click(){
        prod_table.setRowFactory(tv -> {
            TableRow <ProdUseRecord> row = new TableRow<>();
            return row;
        });
    }

}
