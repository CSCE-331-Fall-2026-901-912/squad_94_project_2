package model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;

public class TipPopUp {

    private FloatProperty tip_total_float;
    private StringProperty tip_total;

    // GETTERS
    public StringProperty get_tip_total(){return tip_total;}
    public FloatProperty get_tip_total_float(){return tip_total_float;}

    // SETTERS
    public void tip0(){
        // TODO: Update tip_total
    }
    public void tip10(){
        // TODO: Update tip_total
    }
    public void tip15(){
        // TODO: Update tip_total
    }
    public void tip20(){
        // TODO: Update tip_total
    }
    public void tip25(){
        // TODO: Update tip_total
    }
    public void tipdone(){
        // TODO: update PSQL database, reset current_order data 
        // TODO: close window
    }

    // CONSTRUCTOR
    public TipPopUp(){
        tip_total_float = new SimpleFloatProperty(0.0f);
        tip_total = new SimpleStringProperty("");
    }
}
