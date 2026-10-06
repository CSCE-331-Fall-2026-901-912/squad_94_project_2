package model;

import java.math.BigDecimal;

import database.PGComms;
import dto.EmployeesRowDTO;
import dto.MenuDrinksRowDTO;
import dto.MenuToppingsRowDTO;
import model.AddMenuDrink;
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
    @FXML private TableView <MenuDrinksRowDTO> MenuDrinksTable;
    @FXML private TableColumn <MenuDrinksRowDTO, String> DrinkName;
    @FXML private TableColumn <MenuDrinksRowDTO, BigDecimal> DrinkPrice;
    @FXML private TableColumn <MenuDrinksRowDTO, String> DrinkType;
    @FXML private TableColumn <MenuDrinksRowDTO, Boolean> DrinkHotAvailable;
    @FXML private TableColumn <MenuDrinksRowDTO, Boolean> DrinkNonCaffeinated;

    @FXML private TableView <MenuToppingsRowDTO> MenuToppingsTable;
    @FXML private TableColumn <MenuToppingsRowDTO, String> ToppingName;
    @FXML private TableColumn <MenuToppingsRowDTO, BigDecimal> ToppingPrice;

    public void load_menu() {
        DrinkName.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().name()));
        DrinkPrice.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().price()));
        DrinkType.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().type()));
        DrinkHotAvailable.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().hot_available()));
        DrinkNonCaffeinated.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().is_non_caffeinated()));

        ToppingName.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().name()));
        ToppingPrice.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().price()));

        MenuDrinksTable.setItems(FXCollections.observableArrayList(PGComms.issue_query_type_rows_MenuDrinks(
            "SELECT id_drink, name, price, type, hot_available, is_non_caffeinated "
            + "FROM menu_drinks ORDER BY id_drink")));

        MenuToppingsTable.setItems(FXCollections.observableArrayList(PGComms.issue_query_type_rows_MenuToppings(
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
        MenuDrinksTable.setRowFactory(table -> {
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
        MenuToppingsTable.setRowFactory(table -> {
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
