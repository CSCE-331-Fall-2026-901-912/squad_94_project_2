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

}
