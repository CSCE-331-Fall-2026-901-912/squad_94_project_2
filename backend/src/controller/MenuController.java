package controller;

import dto.MenuDrinksRowDTO;
import dto.MenuToppingsRowDTO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Window;
import javafx.stage.Stage;
import model.MenuModel;

import java.math.BigDecimal;

public class MenuController {
    private final MenuModel model;

    @FXML private TableView<MenuDrinksRowDTO> menu_drinks_table;
    @FXML private TableColumn<MenuDrinksRowDTO, String> drink_name;
    @FXML private TableColumn<MenuDrinksRowDTO, BigDecimal> drink_price;
    @FXML private TableColumn<MenuDrinksRowDTO, String> drink_type;
    @FXML private TableColumn<MenuDrinksRowDTO, Boolean> drink_hot_available;
    @FXML private TableColumn<MenuDrinksRowDTO, Boolean> drink_non_caffeinated;

    @FXML private TableView<MenuToppingsRowDTO> menu_toppings_table;
    @FXML private TableColumn<MenuToppingsRowDTO, String> topping_name;
    @FXML private TableColumn<MenuToppingsRowDTO, BigDecimal> topping_price;

    public MenuController(MenuModel model) {
        this.model = model;
    }

    @FXML
    private void initialize() {
        configure_columns();
        setup_drink_row_click();
        setup_topping_row_click();
        refresh();
    }

    public void refresh() {
        menu_drinks_table.setItems(
            FXCollections.observableArrayList(model.get_drinks()));

        menu_toppings_table.setItems(
            FXCollections.observableArrayList(model.get_toppings()));
    }

    private void configure_columns() {
        drink_name.setCellValueFactory(data ->
            new ReadOnlyObjectWrapper<>(data.getValue().name()));
        drink_price.setCellValueFactory(data ->
            new ReadOnlyObjectWrapper<>(data.getValue().price()));
        drink_type.setCellValueFactory(data ->
            new ReadOnlyObjectWrapper<>(data.getValue().type()));
        drink_hot_available.setCellValueFactory(data ->
            new ReadOnlyObjectWrapper<>(data.getValue().hot_available()));
        drink_non_caffeinated.setCellValueFactory(data ->
            new ReadOnlyObjectWrapper<>(
                data.getValue().is_non_caffeinated()));
        topping_name.setCellValueFactory(data ->
            new ReadOnlyObjectWrapper<>(data.getValue().name()));
        topping_price.setCellValueFactory(data ->
            new ReadOnlyObjectWrapper<>(data.getValue().price()));
    }

    private void open_drink_form(Window owner, MenuDrinksRowDTO existing) {
        AddMenuDrinkController controller =
            new AddMenuDrinkController(
                model,
                this::refresh,
                existing);

        Stage stage = ViewLoader.open_window(
            "/gui/manager/AddMenuDrink.fxml",
            controller,
            owner);

        if (stage != null) {
            stage.setTitle(
                existing == null ? "Add New Drink" : "Edit Drink");
        }
    }

    @FXML
    public void open_add_menu_drink(ActionEvent event) {
        open_drink_form(((Node) event.getSource()).getScene().getWindow(), null);
    }

    private void open_topping_form(Window owner, MenuToppingsRowDTO existing) {
        AddMenuToppingController controller =
            new AddMenuToppingController(
                model,
                this::refresh,
                existing);

        Stage stage = ViewLoader.open_window(
            "/gui/manager/AddMenuTopping.fxml",
            controller,
            owner);

        if (stage != null) {
            stage.setTitle(
                existing == null ? "Add New Topping" : "Edit Topping");
        }
    }

    @FXML
    public void open_add_menu_topping(ActionEvent event) {
        open_topping_form(((Node) event.getSource()).getScene().getWindow(), null);
    }

    private void setup_drink_row_click() {
        menu_drinks_table.setRowFactory(table -> {
            TableRow<MenuDrinksRowDTO> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    open_drink_form(table.getScene().getWindow(), row.getItem());
                }
            });

            return row;
        });
    }

    private void setup_topping_row_click() {
        menu_toppings_table.setRowFactory(table -> {
            TableRow<MenuToppingsRowDTO> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    open_topping_form(table.getScene().getWindow(), row.getItem());
                }
            });

            return row;
        });
    }
}