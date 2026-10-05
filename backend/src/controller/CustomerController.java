package controller;

import model.BaseMenu;
import model.CancellationPopUp;
import model.OrderMenu;
import model.TipPopUp;

import javafx.fxml.FXML;
// import java.sql.*;
import javafx.scene.control.Button;
// import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
// import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Slider;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import database.PGComms;

public class CustomerController {

    // private DataSource dataSource;
    private BaseMenu base_menu;
    private CancellationPopUp cancel_pop_up;
    private OrderMenu order_menu;
    private TipPopUp tip_pop_up;


    // BASEMENU TEXTS & BUTTONS
    @FXML private TextArea current_order;
    @FXML private Label order_total;
    
    @FXML public void add_drink(){
        order_menu = base_menu.add_drink(this);
    }
    @FXML public void clear_order(){
        cancel_pop_up = base_menu.clear_order(this);
    }
    @FXML public void finish_order(){
        tip_pop_up = base_menu.finish_order(this);
    }
    @FXML public void manager_view(){
        base_menu.manager_view(this);
    }

    // CANCELLATION POP UP BUTTONS
    @FXML public void confirm_cancel(){
        cancel_pop_up.confirm_cancel();

        // TODO: clear current_order
    }
    @FXML public void deny_cancel(){
        cancel_pop_up.deny_cancel();

        // TODO: close popup
    }

    // ORDER MENU BUTTONS AND SLIDER
    @FXML private ComboBox<String> milk_tea_box;
    @FXML private ComboBox<String> fresh_tea_box;
    @FXML private ComboBox<String> fruit_tea_box;
    @FXML private ComboBox<String> no_caff_box;

    // Fill each combo box with the menu_drinks rows of the matching type (dynamic adding)
    public void populate_drink_boxes(){
        fill_box(milk_tea_box, "milk tea");
        fill_box(fresh_tea_box, "fresh tea");
        fill_box(fruit_tea_box, "fruit tea");
        fill_box(no_caff_box, "no caff tea");
    }
    private void fill_box(ComboBox<String> box, String type){
        if (box == null) return;
        box.setItems(FXCollections.observableArrayList(PGComms.get_drink_names_by_type(type)));
    }
    
    @FXML private GridPane topping_grid;
    //allows for dynamic adding of toppings
    public void populate_topping_buttons(){
        if (topping_grid == null) return;          // not the OrderMenu view
        topping_grid.getChildren().clear();

        List<String> toppings = PGComms.get_topping_names();
        for (int i = 0; i < toppings.size(); i++) {
            String name = toppings.get(i);

            Button b = new Button(name);
            b.setPrefSize(95.0, 45.0);
            b.setWrapText(true);
            b.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            b.setFont(new Font(10.0));
            b.setOnAction(e -> topping_selected(name));

            topping_grid.add(b, i % 2, i / 2);     // column = i % 2, row = i / 2
        }
    }

    private void topping_selected(String topping_name){
        // TODO: add this topping to the current drink (max 2, since orders has id_topping1 and id_topping2)
    }
    @FXML private Slider sugar_slider;
    @FXML private Label sugar_label;

    public void setup_sugar_slider(){
        if (sugar_slider == null || sugar_label == null) return;   // not the OrderMenu view
        sugar_label.textProperty().bind(
            Bindings.createStringBinding(
                () -> "Sugar: " + (int) Math.round(sugar_slider.getValue()) + "%",
                sugar_slider.valueProperty()));
    }

    // Use this when you save the order. It is always 0, 25, 50, 75 or 100.
    public int get_sugar_level(){
        return (int) Math.round(sugar_slider.getValue());
    }
    
    // TIP POP UP BUTTONS
    @FXML private Label tip_total;

    @FXML public void tip0(){
        tip_pop_up.tip(1.0);
    }
    @FXML public void tip10(){
        tip_pop_up.tip(1.10);
    }
    @FXML public void tip15(){
        tip_pop_up.tip(1.15);
    }
    @FXML public void tip20(){
        tip_pop_up.tip(1.2);
    }
    @FXML public void tip25(){
        tip_pop_up.tip(1.25);
    }
    @FXML public void tipdone(){
        if (tip_pop_up.tipdone(order_total.toString())){
            base_menu.set_current_order("Item1: ");
            base_menu.set_order_total("Total: 0.00");
        }
        // TODO: update PSQL database, reset current_order data 
        // TODO: close window
    }


    public void initialize(BaseMenu base_menu){
        this.base_menu = base_menu;
        current_order.textProperty().bind(base_menu.get_current_order());
        order_total.textProperty().bind(base_menu.get_order_total());
    }
    public void initialize(TipPopUp tip_pop_up){
        this.tip_pop_up = tip_pop_up;
        tip_total.textProperty().bind(tip_pop_up.get_tip_total());
    }
}
