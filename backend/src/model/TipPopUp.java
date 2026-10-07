package model;

import javafx.beans.property.StringProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
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
    public boolean tipdone(String current_order){
        // TODO: update PSQL database
        int num_orders = 0;
        order_id = get_last_orderID() + 1;
        for (int i = 0; i < num_orders; i++){
            process_order_string(order_id); 
            // PGComms.insert_order_item(order_id, ., tip_percent, id_drink, id_topping1, id_topping2, ice_level, sugar_level, hot_chosen);
        }
        stage_tip.close();
        return true;
    }

    private ArrayList<String> process_order_string(int order_id){
        String order_string = current_order_string;

        String drink_name = "";
        String id_topping1_name = "";
        String id_topping2_name = "";
        String ice_level_name = "";
        String sugar_level_name = "";


        // TODO: match ice_level

        // TODO: match sugar_level

        id_drink = PGComms.issue_query_type_rows_MenuDrinks("SELECT "+drink_name+" FROM menu_drinks;").getFirst().id_drink();
        id_topping1 = PGComms.issue_query_type_rows_InvEdible("SELECT "+id_topping1_name+" FROM menu_drinks;").getFirst().id_edible();
        id_topping2 = PGComms.issue_query_type_rows_InvEdible("SELECT "+id_topping2_name+" FROM menu_drinks;").getFirst().id_edible();
        ice_level = 2;
        sugar_level = 4;
        
        PGComms.insert_order_item(order_id, Double.parseDouble(order_total_val), tip_percent, id_drink, id_topping1, id_topping2, ice_level, sugar_level, hot_chosen);
        
        return new ArrayList<String>(); //order_string;
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