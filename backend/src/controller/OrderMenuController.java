package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.beans.binding.Bindings;

import model.CurrentOrderModel;
import model.MenuModel;
import dto.OrderLineDTO;

/**
 * Controller for the order menu, allowing users to select a drink, toppings, sugar level, and ice level.
 * The controller populates the drink and topping options from the menu model, manages user selections, and adds the selected order line to the current order model.
 * 
 * @author Squad 94 - Sanjana Ram
 */
public class OrderMenuController {
    private final CurrentOrderModel order_model;
    private final MenuModel menu_model;

    // ComboBoxes for drink selection
    @FXML private ComboBox<String> milk_tea_box;
    @FXML private ComboBox<String> fresh_tea_box;
    @FXML private ComboBox<String> fruit_tea_box;
    @FXML private ComboBox<String> no_caff_box;
    @FXML private ComboBox<String> seasonal_box;

    // GridPane for topping buttons
    @FXML private GridPane topping_grid;
    private static final int MAX_TOPPINGS = 2;
    private static final String SELECTED_STYLE =
        "-fx-background-color: #2e9e5b; -fx-text-fill: white; -fx-font-weight: bold;";
    private final List<String> selected_toppings = new ArrayList<>();
    private final Set<String> sold_out_drinks = new HashSet<>();

    // Slider and Label for sugar level selection
    @FXML private Slider sugar_slider;
    @FXML private Label sugar_label;

    // Buttons for ice level selection
    @FXML private Label selected_drink_label;
    @FXML private Button no_ice_button;
    @FXML private Button light_ice_button;
    @FXML private Button regular_ice_button;
    @FXML private Button hot_button;

    /**
     * Creates an order menu controller with the given current order model and menu model.
     * @param order_model the current order model used to add the selected order line
     * @param menu_model the menu model used to retrieve drink and topping options
     */
    public OrderMenuController(CurrentOrderModel order_model, MenuModel menu_model) {
        this.order_model = order_model;
        this.menu_model = menu_model;
    }

    /**
     * Initializes the order menu view by populating the drink combo boxes, topping buttons, setting up the sugar slider, and setting up drink selection handling.
     */
    @FXML
    private void initialize() {
        populate_drink_boxes();
        populate_topping_buttons();
        setup_sugar_slider();
        setup_drink_selection();
    }

    /**
     * Populates the drink combo boxes with available drinks from the menu model, grouped by type.
     */
    public void populate_drink_boxes(){
        fill_box(milk_tea_box, "milk tea");
        fill_box(fresh_tea_box, "fresh tea");
        fill_box(fruit_tea_box, "fruit tea");
        fill_box(no_caff_box, "no caff tea");
        fill_box(seasonal_box, "seasonal");
    }

    /**
     * Populates a given combo box with drink names of a specific type from the menu model, and configures the combo box to disable sold-out drinks.
     * @param box the combo box to populate
     * @param type the type of drinks to populate the combo box with (e.g., "milk tea", "fresh tea")
     */
    private void fill_box(ComboBox<String> box, String type){
        if (box == null) return;
        List<String> drink_names = menu_model.get_drink_names_by_type(type);
        sold_out_drinks.addAll(menu_model.get_sold_out_drink_names_by_type(type));
        box.setItems(FXCollections.observableArrayList(drink_names));
        configure_drink_cells(box);
    }

    /**
     * Configures the cells of a combo box to disable and grey out sold-out drinks, preventing users from selecting them.
     * @param box the combo box to configure
     */
    private void configure_drink_cells(ComboBox<String> box) {
        box.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                boolean sold_out = !empty && name != null && sold_out_drinks.contains(name);
                setText(empty ? null : name);
                setDisable(sold_out);
                setMouseTransparent(sold_out);
                setStyle(sold_out
                        ? "-fx-text-fill: grey;" : "");
            }
        });
        box.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                setText(empty ? box.getPromptText() : name);
                setStyle(!empty && name != null && sold_out_drinks.contains(name)
                        ? "-fx-text-fill: grey;" : "");
            }
        });
    }

    /**
     * Populates the topping buttons in the grid pane with available toppings from the menu model
     */
    public void populate_topping_buttons(){
        if (topping_grid == null) return;  
        selected_toppings.clear();         
        topping_grid.getChildren().clear();

        List<String> toppings = menu_model.get_topping_names();
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

    /**
     * Handles the selection of a topping button, adding or removing the topping from the selected toppings list and 
     * refreshing the button styles as long as the maximum number of toppings has not been reached.
     * @param name the name of the topping that was selected or deselected
     */
    private void topping_selected(String name){
        if (selected_toppings.contains(name)) {
            selected_toppings.remove(name);               
        } else if (selected_toppings.size() < MAX_TOPPINGS) {
            selected_toppings.add(name);
        }
        refresh_topping_buttons();
    }

    /**
     * Refreshes the styles and enabled/disabled state of the topping buttons based on the current selection of toppings and the maximum allowed toppings.
     */
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

    /**
     * Returns a copy of the list of selected toppings.
     * @return a list of selected topping names
     */
    public List<String> get_selected_toppings(){
        return List.copyOf(selected_toppings);
    }

    /**
     * Sets up the sugar slider to display the current sugar level as a percentage in the associated label.
     * The label is updated whenever the slider value changes.
     */
    public void setup_sugar_slider(){
        if (sugar_slider == null || sugar_label == null) return;   
        sugar_label.textProperty().bind(
            Bindings.createStringBinding(
                () -> "Sugar: " + (int) Math.round(sugar_slider.getValue()) + "%",
                sugar_slider.valueProperty()));
    }

    /**
     * Returns the current sugar level selected by the user, rounded to the nearest integer (should always be 0, 25, 50, 75, or 100).
     * @return the sugar level as an integer percentage (0-100)
     */
    public int get_sugar_level(){
        return (int) Math.round(sugar_slider.getValue());
    }
    

    private String selected_drink;
    /**
     * Returns a list of all the drink combo boxes in the order menu, allowing for easy iteration and management of drink selection.
     * @return
     */
    private List<ComboBox<String>> drink_boxes(){
        return List.of(milk_tea_box, fresh_tea_box, fruit_tea_box, no_caff_box, seasonal_box);
    }

    /**
     * Sets up the event handlers for the drink combo boxes to manage drink selection, ensuring that only one drink can be selected at a time and that sold-out drinks cannot be selected.
     * When a drink is selected, the other combo boxes are cleared, and the selected drink is stored in the controller's state.
     */
    public void setup_drink_selection(){
           selected_drink = null;
            selected_ice = null;
        if (milk_tea_box == null) return;           
        for (ComboBox<String> box : drink_boxes()) {
            box.setOnAction(e -> drink_picked(box));
        }
    }

    // When a drink is selected, this method is called to handle the selection, ensuring that only one drink can be selected at a time and that sold-out drinks cannot be selected.
    private void drink_picked(ComboBox<String> source){
        String name = source.getValue();
        if (name == null) return;
        if (sold_out_drinks.contains(name)) {
            source.setValue(null);
            new Alert(
                    Alert.AlertType.WARNING,
                    "This drink cannot be selected because one or more ingredients are out of stock.")
                    .showAndWait();
            return;
        }

        // Only one drink can be chosen, so clear the other three boxes.
        for (ComboBox<String> box : drink_boxes()) {
            if (box != source) box.setValue(null);
        }

        selected_drink = name;
        selected_drink_label.setText("Selected: " + name);

        boolean hot = menu_model.is_hot_available(name);
        hot_button.setVisible(hot);
        hot_button.setManaged(hot);
    }

    private String selected_ice;
    /**
     * Handles the selection of an ice level button, updating the selected ice level and refreshing the styles of the ice level buttons to indicate which one is currently selected.
     * @param event the action event triggered by clicking an ice level button
     */
    @FXML public void ice_selected(ActionEvent event){
        Button clicked = (Button) event.getSource();

        // "None", "Light", "Regular" or "Hot"
        selected_ice = clicked.getText();
        for (Button b : List.of(no_ice_button, light_ice_button, regular_ice_button, hot_button)) {

            // Reuse the green from the toppings.
            b.setStyle(b == clicked ? SELECTED_STYLE : "");
        }
    }
    
    /**
     * Finalizes the selection of the drink, toppings, sugar level, and ice level, creating a new order line and adding it to the current order model.
     * If the user has not selected a drink or ice level, a warning alert is shown and the order is not added.
     * The order menu window is closed after the order line is added to the current order model.
     * @param event the action event triggered by clicking the "Finish Selection" button
     */
    @FXML public void finish_selection(ActionEvent event){
        System.out.println("drink=" + selected_drink + ", ice=" + selected_ice);
        if (selected_drink == null || selected_ice == null) {
            new Alert(Alert.AlertType.WARNING, "Please choose a drink and an ice level.").showAndWait();
            return;
        }

        String topping1 = selected_toppings.size() > 0 ? selected_toppings.get(0) : "No topping";
        String topping2 = selected_toppings.size() > 1 ? selected_toppings.get(1) : "No topping";

        BigDecimal price = menu_model.get_drink_price(selected_drink);
        for (String t : selected_toppings) {
            price = price.add(menu_model.get_topping_price(t));
        }

        order_model.add_item(new OrderLineDTO(
            selected_drink,
            topping1,
            topping2,
            selected_ice,
            get_sugar_level(),
            price));
        
        order_model.refresh_display();
        // Close the order menu window.
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    /**
     * Closes the order menu window without adding an order line to the current order model.
     * @param event the action event triggered by clicking the "Cancel Order" button
     */
    @FXML
    public void cancel_order(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

}
