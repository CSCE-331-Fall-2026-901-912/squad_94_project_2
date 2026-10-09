package model;

import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.ArrayList;

import controller.ManagerController;
import database.PGComms;
import dto.InvEdibleRowDTO;
import dto.InvNonEdibleRowDTO;
import javafx.scene.Scene;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class AddItemPopUp {
    // private StringProperty input_name;
    // private StringProperty input_quantity;

    // public StringProperty get_input_name(){ return input_name; }
    // public StringProperty get_input_quantity(){ return input_quantity; }

    // public void set_input_name(String n_name){ input_name.set(n_name); }
    // public void set_input_quantity(String n_quantity){ input_quantity.set(n_quantity); }


    public void find_and_change_item(String name, String quantity){

        String name_non_edible_query = "SELECT * FROM inv_nonedible WHERE name LIKE '"+name+"';";
        String name_edible_query = "SELECT * FROM inv_edible WHERE name LIKE '"+name+"';";
        //System.out.println("after the query");
        ArrayList<InvNonEdibleRowDTO> arr_non_edible = PGComms.issue_query_type_rows_InvNonEdible(name_non_edible_query);
        ArrayList<InvEdibleRowDTO> arr_edible = PGComms.issue_query_type_rows_InvEdible(name_edible_query);
        //System.out.println("after arrs");
        if(!arr_non_edible.isEmpty()){
            //System.out.println("b4 noned");
            int non_edible_id = arr_non_edible.getFirst().id_nonedible();
            PGComms.modify_topping_quantity_in_nonedible_inventory(non_edible_id, (int) Double.parseDouble(quantity));
            //System.out.println("after noned");
        }
        else{
            //System.out.println("b4 ed");
            //System.out.println(arr_edible.size());
            int edible_id = arr_edible.getFirst().id_edible();
            //System.out.println("b4 pgcomms");
            PGComms.modify_topping_quantity_in_edible_inventory(edible_id, (int) Double.parseDouble(quantity));
            //System.out.println("after ed");
        }
        //System.out.println("after both ifs");

    }


    public AddItemPopUp submit_changes_button(String name, String quantity){
        find_and_change_item(name, quantity);
        add_item_stage.close();
        return this;
    }

    Stage add_item_stage;
    public AddItemPopUp(Stage stage){
        add_item_stage = stage;

    }
}
