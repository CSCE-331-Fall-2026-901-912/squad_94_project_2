package model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ItemDetailsPopUp {
    

    private StringProperty item_name;
    private IntegerProperty item_amount;

    public StringProperty get_current_item_name(){return item_name;}
    public IntegerProperty get_current_amount(){return item_amount;}

    @FXML public void set_amount(IntegerProperty amount_input){
        item_amount = amount_input;
        //TODO Link sql 
    }


    public ItemDetailsPopUp(){
        item_name = new SimpleStringProperty("");
        item_amount = new SimpleIntegerProperty(0);
    }
}
