package controller;

import model.BaseMenu;
import model.CancellationPopUp;
import model.OrderMenu;
import model.TipPopUp;

import dto.InvEdibleRowDTO;
import dto.InvNonEdibleRowDTO;
import dto.EmployeesRowDTO;
import dto.MenuDrinksRowDTO;
import dto.MenuToppingsRowDTO;
import dto.JoinMenuDrinksAndInvEdibleRowDTO;
import dto.JoinMenuToppingsAndInvEdibleRowDTO;
import dto.OrdersRowDTO;

import javafx.fxml.FXML;
// import java.sql.*;
import javafx.scene.control.Button;
// import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
// import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Slider;
import javafx.scene.Node;
import java.math.BigDecimal;

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
        base_menu.reset_order();
        cancel_pop_up.confirm_cancel();
    }
    @FXML public void deny_cancel(){
        cancel_pop_up.deny_cancel();
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
    private static final int MAX_TOPPINGS = 2;
    private static final String SELECTED_STYLE =
        "-fx-background-color: #2e9e5b; -fx-text-fill: white; -fx-font-weight: bold;";
    private final List<String> selected_toppings = new ArrayList<>();

    //allows for dynamic adding of toppings
    public void populate_topping_buttons(){
        if (topping_grid == null) return;  
        selected_toppings.clear();         
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

    private void topping_selected(String name){
        if (selected_toppings.contains(name)) {
            selected_toppings.remove(name);               
        } else if (selected_toppings.size() < MAX_TOPPINGS) {
            selected_toppings.add(name);
        }
        refresh_topping_buttons();
    }

    // Color the chosen buttons, and once the max is reached, grey out the rest.
    private void refresh_topping_buttons(){
        boolean full = selected_toppings.size() >= MAX_TOPPINGS;
        for (Node n : topping_grid.getChildren()) {
            Button b = (Button) n;
            boolean picked = selected_toppings.contains(b.getText());
            if(picked){
                b.setStyle(SELECTED_STYLE);
            }
            b.setDisable(full && !picked);
        }
    }

    // For saving the order later: 0, 1 or 2 topping names, in the order they were picked.
    public List<String> get_selected_toppings(){
        return List.copyOf(selected_toppings);
    }

    @FXML private Slider sugar_slider;
    @FXML private Label sugar_label;

    public void setup_sugar_slider(){
        if (sugar_slider == null || sugar_label == null) return;   
        sugar_label.textProperty().bind(
            Bindings.createStringBinding(
                () -> "Sugar: " + (int) Math.round(sugar_slider.getValue()) + "%",
                sugar_slider.valueProperty()));
    }

    // Use this when you save the order. It is always 0, 25, 50, 75 or 100.
    public int get_sugar_level(){
        return (int) Math.round(sugar_slider.getValue());
    }

    @FXML private Label selected_drink_label;
    @FXML private Button no_ice_button;
    @FXML private Button light_ice_button;
    @FXML private Button regular_ice_button;
    @FXML private Button hot_button;
    private String selected_drink;

    private List<ComboBox<String>> drink_boxes(){
        return List.of(milk_tea_box, fresh_tea_box, fruit_tea_box, no_caff_box);
    }

    // Wire all four combo boxes to the same handler.
    public void setup_drink_selection(){
           selected_drink = null;
            selected_ice = null;
        if (milk_tea_box == null) return;           
        for (ComboBox<String> box : drink_boxes()) {
            box.setOnAction(e -> drink_picked(box));
        }
    }

    private void drink_picked(ComboBox<String> source){
        String name = source.getValue();
        if (name == null) return;                 

        // Only one drink can be chosen, so clear the other three boxes.
        for (ComboBox<String> box : drink_boxes()) {
            if (box != source) box.setValue(null);
        }

        selected_drink = name;
        selected_drink_label.setText("Selected: " + name);

        boolean hot = PGComms.is_hot_available(name);
        hot_button.setVisible(hot);
        hot_button.setManaged(hot);
    }

    private String selected_ice;

    @FXML public void ice_selected(ActionEvent event){
        Button clicked = (Button) event.getSource();
        selected_ice = clicked.getText();                    // "None", "Light", "Regular" or "Hot"
        for (Button b : List.of(no_ice_button, light_ice_button, regular_ice_button, hot_button)) {
            b.setStyle(b == clicked ? SELECTED_STYLE : "");  // reuse the green from the toppings
        }
    }
    
    @FXML public void finish_selection(ActionEvent event){
        System.out.println("drink=" + selected_drink + ", ice=" + selected_ice);
        if (selected_drink == null || selected_ice == null) {
            new Alert(Alert.AlertType.WARNING, "Please choose a drink and an ice level.").showAndWait();
            return;
        }

        String topping1 = selected_toppings.size() > 0 ? selected_toppings.get(0) : "No topping";
        String topping2 = selected_toppings.size() > 1 ? selected_toppings.get(1) : "No topping";

        BigDecimal price = PGComms.get_drink_price(selected_drink);
        for (String t : selected_toppings) {
            price = price.add(PGComms.get_topping_price(t));
        }

        base_menu.add_item(
            selected_drink + ", " + topping1 + ", " + topping2 + ", "
            + selected_ice + ", " + get_sugar_level() + "%",
            price);

        // Close the order menu window.
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML public void cancel_order(ActionEvent event){
        ((Node) event.getSource()).getScene().getWindow().hide();
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
            base_menu.reset_order();
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
    public void initialize(CancellationPopUp cancel_pop_up){
        this.cancel_pop_up = cancel_pop_up;
    }
}
