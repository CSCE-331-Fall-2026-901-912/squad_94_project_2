package model;

// import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.CustomerController;
import javafx.beans.property.FloatProperty;
// import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;

import javafx.scene.control.Label;
import java.math.BigDecimal;


public class BaseMenu {

    private StringProperty current_order;
    private StringProperty order_total;

    private int item_count = 0;
    private BigDecimal running_total = BigDecimal.ZERO;
    
    // GETTERS
    public StringProperty get_current_order(){return current_order;}
    public StringProperty get_order_total(){return order_total;}

    public void set_current_order(String current_order2){current_order.set(current_order2);}
    public void set_order_total(String order_total2){order_total.set(order_total2);}

    // SETTERS

    // Append one finished drink to the order text area and update the total.
    public void add_item(String description, BigDecimal price){
        item_count++;
        running_total = running_total.add(price);

        String line = "Item " + item_count + ": " + description + " : " + String.format("%.2f", price);
        if (item_count == 1) {
            set_current_order(line);                      // replaces the "Item1: " placeholder
        } else {
            set_current_order(get_current_order() + "\n" + line);
        }
        set_order_total("Total: " + String.format("%.2f", running_total));
    }

    // Call this after the order is paid or cancelled.
    public void reset_order(){
        item_count = 0;
        running_total = BigDecimal.ZERO;
        set_current_order("Item1: ");
        set_order_total("Total: 0.00");
    }
    
    @FXML public OrderMenu add_drink(CustomerController customer_controller){
        try {
            Stage stage_order = new Stage();
            FXMLLoader order_menu = new FXMLLoader(getClass().getResource("/gui/cashier/OrderMenu.fxml"));
            OrderMenu order_menu_model = new OrderMenu();
            order_menu.setController(customer_controller);

            Parent root = order_menu.load();
            customer_controller.populate_drink_boxes();
            customer_controller.populate_topping_buttons();  
            customer_controller.setup_sugar_slider();
            customer_controller.setup_drink_selection();
            Scene scene = new Scene(root);
            
            stage_order.setScene(scene);
            stage_order.show();
            return order_menu_model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new OrderMenu();
        
    }
    @FXML public CancellationPopUp clear_order(CustomerController customer_controller){
        try {
            Stage stage_cancel = new Stage();
            FXMLLoader cancellation_pop_up = new FXMLLoader(getClass().getResource("/gui/cashier/Cancellation.fxml"));
            CancellationPopUp cancellation_pop_up_model = new CancellationPopUp();
            cancellation_pop_up.setController(customer_controller);

            Parent root = cancellation_pop_up.load();
            Scene scene = new Scene(root);

            stage_cancel.setScene(scene);
            stage_cancel.show();
            return cancellation_pop_up_model;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new CancellationPopUp();
    }
    @FXML public TipPopUp finish_order(CustomerController customer_controller){
        try {
            
            Stage stage_tip = new Stage();
            FXMLLoader tip_pop_up = new FXMLLoader(getClass().getResource("/gui/cashier/TotalPopUp.fxml"));
            tip_pop_up.setController(customer_controller);
            
            Parent root = tip_pop_up.load();
            Scene scene = new Scene(root);

            stage_tip.setScene(scene);
            stage_tip.show();
            TipPopUp tip_pop_up_model = new TipPopUp(order_total.getValue(), stage_tip); 
            customer_controller.initialize(tip_pop_up_model);
            return tip_pop_up_model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new TipPopUp(null,null);
    }
    @FXML public void manager_view(CustomerController manager_controller){
        order_total.set("null");
        try {
            // Stage manager_stage = new Stage();
            // FXMLLoader manager_view = new FXMLLoader(getClass().getResource("/gui/cashier/TipPopUp.fxml"));
            // ManagerView manager_view_model = new BaseMenu();
            // manager_view.setController(manager_controller);
            
            // Parent root = manager_view.load();
            // Scene scene = new Scene(root);

            // manager_stage.setScene(scene);
            // manager_stage.show();
            // return manager_view_model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return new ManagerView();
    }

    // CONSTRUCTOR
    public BaseMenu(){
        current_order = new SimpleStringProperty("Item1: ");
        order_total = new SimpleStringProperty("Total: 0.00");
    }
}
