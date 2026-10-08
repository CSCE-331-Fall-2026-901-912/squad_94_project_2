package model;

import javafx.beans.property.StringProperty;

// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;

// import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import java.util.ArrayList;
import database.PGComms;
import dto.*;


public class TipPopUp {

    private StringProperty tip_total;
    private Double tip_total_val;
    private String order_total_val;
    private String current_order_string;
    private Stage stage_tip;

    private int order_id;
    private double tip_percent;
    private int id_drink;
    private int id_topping1;
    private int id_topping2;
    private int ice_level;
    private int sugar_level;
    private boolean hot_chosen;

    // GETTERS
    public StringProperty get_tip_total(){return tip_total;}

    // SETTERS
    public void tip(Double tip){
        tip_percent = (Double.parseDouble("0"+tip.toString().substring(1)));
        tip_total_val = (Math.round((Double.parseDouble(order_total_val) * tip) * 100) / 100.0);
        tip_total.set("Total: " + tip_total_val);
    }
    public boolean tipdone(){

        // TODO: update PSQL database
        // I believe this task has been completed? - Rigo
        int num_orders = 0;

        
        // System.err.println("Marker -1");
        String[] order_lines = current_order_string.split("\n");
        num_orders = order_lines.length;
        if(order_lines[0].equals("Item1: ")){
            num_orders = 0;
            stage_tip.close();
            reset_order_data();
            System.err.println("No order to finish.");
            return false;
        }
        // System.err.println(":"+order_lines[0]+":");
        

        System.err.println("Marker 0");
        for (int i = 0; i < num_orders; i++){
            order_id = get_last_orderID() + 1;
            String trimmed = order_lines[i].substring(order_lines[i].indexOf(":")+1, order_lines[i].indexOf("%")+2).trim();
            // System.out.println("Marker 1: ");
            double price = Double.parseDouble(order_lines[i].substring(order_lines[i].indexOf("%")+3).trim()) * (1+tip_percent);
            // System.out.println("Marker 2: ");
            process_order_string(order_id, trimmed, price); 
            // System.out.println("Processing current order: " + current_order_string);
            // System.out.println("Processing order line: " + trimmed);
            // PGComms.insert_order_item(order_id, ., tip_percent, id_drink, id_topping1, id_topping2, ice_level, sugar_level, hot_chosen);
        }
        System.err.println("Marker 02");
        stage_tip.close();
        reset_order_data();
        return true;
    }

    private void process_order_string(int order_id, String process_order_string, double price){
        // System.err.println("Marker 1");
        String[] order_items = process_order_string.split(",");


        // System.err.println("Marker 2");
        String drink_name = order_items[0].trim();
        String id_topping1_name = order_items[1].trim();
        String id_topping2_name = order_items[2].trim();
        String ice_level_name = order_items[3].trim();
        String sugar_level_name = order_items[4].trim();
        // System.err.println("Marker 3");

        // Item: drink_name, id_topping1, id_topping2, ice_level, sugar_level : 0.00
        
        switch(ice_level_name){
            case "None": ice_level = 0; break;
            case "Light": ice_level = 1; break;
            case "Regular": ice_level = 2; break;
            case "Hot": ice_level = 0; hot_chosen = true; break;
            default: ice_level = 2; hot_chosen = false; break;
        }
        switch(sugar_level_name){
            case "0%": sugar_level = 0; break;
            case "25%": sugar_level = 1; break;
            case "50%": sugar_level = 2; break;
            case "75%": sugar_level = 3; break;
            case "100%": sugar_level = 4; break;
            default: sugar_level = 4; break;
        }

        id_drink = PGComms.issue_query_type_rows_MenuDrinks("SELECT * FROM menu_drinks WHERE name LIKE '"+drink_name+"';").getFirst().id_drink();

        // Handle removal of inv_edible amounts based upon drink.
        ArrayList<Integer> id_edibles = PGComms.get_id_edibles_for_drink(id_drink);
        for (int id_edible : id_edibles){
            PGComms.modify_topping_quantity_in_edible_inventory(id_edible, -1);
        }


        System.err.println("Marker 4");
        
        if(!id_topping1_name.equals("No topping")){
            System.err.println("top1");   
            id_topping1 = PGComms.issue_query_type_rows_MenuToppings("SELECT * FROM menu_toppings WHERE name LIKE '"+id_topping1_name+"';").getFirst().id_topping();
            int id_edible1 = PGComms.issue_query_type_rows_JoinMenuToppingsAndInvEdible("SELECT * FROM join_menu_toppings_and_inv_edible WHERE id_topping = " + id_topping1).getFirst().id_edible();

            System.err.println("Marker 4.5");
            // int topping_amount = PGComms.issue_query_type_rows_InvEdible("SELECT * FROM inv_edible WHERE name LIKE '"+id_topping1_name+"';").getFirst().amount_servings();
            System.err.println("Marker 4.6");
            PGComms.modify_topping_quantity_in_edible_inventory(id_edible1, -1);
            System.err.println("Marker 4.7");
        }
        System.err.println("Marker 4.8");
        if(!id_topping2_name.equals("No topping")){
            // System.err.println("top2");   
            id_topping2 = PGComms.issue_query_type_rows_MenuToppings("SELECT * FROM menu_toppings WHERE name LIKE '"+id_topping2_name+"';").getFirst().id_topping();
            int id_edible2 = PGComms.issue_query_type_rows_JoinMenuToppingsAndInvEdible("SELECT * FROM join_menu_toppings_and_inv_edible WHERE id_topping = " + id_topping2).getFirst().id_edible();
            // int topping_amount = PGComms.issue_query_type_rows_InvEdible("SELECT * FROM inv_edible WHERE name LIKE '"+id_topping2_name+"';").getFirst().amount_servings();
            PGComms.modify_topping_quantity_in_edible_inventory(id_edible2, -1);
        }

        PGComms.modify_topping_quantity_in_nonedible_inventory(1,-1);
        PGComms.modify_topping_quantity_in_nonedible_inventory(2,-1);
        PGComms.modify_topping_quantity_in_nonedible_inventory(3,-1);
        PGComms.modify_topping_quantity_in_nonedible_inventory(4,-1);
        
        System.err.println("Marker 5");
        PGComms.insert_order_item(order_id, price, tip_percent, id_drink, id_topping1, id_topping2, ice_level, sugar_level, hot_chosen);
        
        System.err.println("Marker 6");
        return; 
    }

    // CONSTRUCTOR
    public TipPopUp(String order_total, String current_order, Stage stage){
        stage_tip = stage;
        order_total_val = order_total.substring(order_total.indexOf(" ", 0)+1);
        tip_total = new SimpleStringProperty(order_total);

        current_order_string = current_order;
        
        tip_percent = 0.0;
        order_id = 0;
        id_drink = 0;
        id_topping1 = 0;
        id_topping2 = 0;
        ice_level = 2;
        sugar_level = 4;
        hot_chosen = false;
    }

    private int get_last_orderID(){
        ArrayList<OrdersRowDTO> order_rows;
        order_rows = PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order DESC LIMIT 1;");
        
        int last_num = order_rows.getFirst().id_order();
        return last_num;
    }

    private void reset_order_data(){
        tip_percent = 0.0;
        order_id = 0;
        id_drink = 0;
        id_topping1 = 0;
        id_topping2 = 0;
        ice_level = 2;
        sugar_level = 4;
        hot_chosen = false;
    }
}