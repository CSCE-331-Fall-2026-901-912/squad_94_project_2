package controller;

import java.math.BigDecimal;

import dto.MenuDrinksRowDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProdSalesScreenController {

    private record ProdSalesRecord(Integer id, String name, String totalSales, String inventoryUsed) {}

    @FXML private TextField from_year;
    @FXML private TextField from_month;
    @FXML private TextField from_day;

    @FXML private TextField to_year;
    @FXML private TextField to_month;
    @FXML private TextField to_day;


    @FXML private TableView<ProdSalesRecord> whole_table;
    @FXML private TableColumn<ProdSalesRecord, Integer> id_column;
    @FXML private TableColumn<ProdSalesRecord, String> name_column;
    @FXML private TableColumn<ProdSalesRecord, String> total_sales_column;
    @FXML private TableColumn<ProdSalesRecord, String> inventory_used_column;
    
}
