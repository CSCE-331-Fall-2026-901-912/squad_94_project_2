package model;

import java.math.BigDecimal;

import database.PGComms;
import dto.MenuDrinksRowDTO;
import dto.MenuToppingsRowDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableRow;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

public class Menu {
    @FXML private TableView <MenuDrinksRowDTO> menu_drinks_table;
    @FXML private TableColumn <MenuDrinksRowDTO, String> drink_name;
    @FXML private TableColumn <MenuDrinksRowDTO, BigDecimal> drink_price;
    @FXML private TableColumn <MenuDrinksRowDTO, String> drink_type;
    @FXML private TableColumn <MenuDrinksRowDTO, Boolean> drink_hot_available;
    @FXML private TableColumn <MenuDrinksRowDTO, Boolean> drink_non_caffeinated;

    @FXML private TableView <MenuToppingsRowDTO> menu_toppings_table;
    @FXML private TableColumn <MenuToppingsRowDTO, String> topping_name;
    @FXML private TableColumn <MenuToppingsRowDTO, BigDecimal> topping_price;

    public void load_menu() {
        drink_name.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().name()));
        drink_price.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().price()));
        drink_type.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().type()));
        drink_hot_available.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().hot_available()));
        drink_non_caffeinated.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().is_non_caffeinated()));

        topping_name.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().name()));
        topping_price.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().price()));

        menu_drinks_table.setItems(FXCollections.observableArrayList(PGComms.issue_query_type_rows_MenuDrinks(
            "SELECT id_drink, name, price, type, hot_available, is_non_caffeinated "
            + "FROM menu_drinks ORDER BY id_drink")));

        menu_toppings_table.setItems(FXCollections.observableArrayList(PGComms.issue_query_type_rows_MenuToppings(
            "SELECT id_topping, name, price "
            + "FROM menu_toppings ORDER BY id_topping")));

        setup_drink_row_click();
        setup_topping_row_click();
    }

    @FXML public void open_add_menu_drink(ActionEvent event){
        show_menu_form(((Node) event.getSource()).getScene().getWindow(), null);
    }

    private void show_menu_form(Window owner, MenuDrinksRowDTO existing){
        try {
            java.net.URL url = getClass().getResource("/gui/manager/AddMenuDrink.fxml");
            if (url == null) {
                System.out.println("AddMenuDrink.fxml not found");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(new AddMenuDrink(this::load_menu, existing));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(existing == null ? "Add New Drink" : "Edit Drink");
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setup_drink_row_click() {
        menu_drinks_table.setRowFactory(table -> {
            TableRow<MenuDrinksRowDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    show_menu_form(table.getScene().getWindow(), row.getItem());
                }
            });
            return row;
        });
    }

    @FXML
    public void open_add_menu_topping(ActionEvent event) {
        show_topping_form(((Node) event.getSource()).getScene().getWindow(), null);
    }

    private void show_topping_form(Window owner, MenuToppingsRowDTO existing) {
        try {
            java.net.URL url = getClass().getResource("/gui/manager/AddMenuTopping.fxml");
            if (url == null) {
                System.out.println("AddMenuTopping.fxml not found");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(new AddMenuTopping(this::load_menu, existing));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(existing == null ? "Add New Topping" : "Edit Topping");
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setup_topping_row_click() {
        menu_toppings_table.setRowFactory(table -> {
            TableRow<MenuToppingsRowDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    show_topping_form(table.getScene().getWindow(), row.getItem());
                }
            });
            return row;
        });
    }

}
