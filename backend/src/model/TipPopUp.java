package model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class TipPopUp {

    private StringProperty tip_total;
    private Double tip_total_val;
    private String order_total_val;
    private Stage stage_tip;

    // GETTERS
    public StringProperty get_tip_total(){return tip_total;}

    // SETTERS
    public void tip(Double tip){
        tip_total_val = (Math.round((Double.parseDouble(order_total_val) * tip) * 100) / 100.0);
        tip_total.set("Total: " + tip_total_val);
    }
    public boolean tipdone(String current_order){
        // TODO: update PSQL database, reset current_order data 
        stage_tip.close();
        return true;
    }

    // CONSTRUCTOR
    public TipPopUp(String order_total, Stage stage){
        stage_tip = stage;
        order_total_val = order_total.substring(order_total.indexOf(" ", 0)+1);
        tip_total = new SimpleStringProperty(order_total);
    }
}
