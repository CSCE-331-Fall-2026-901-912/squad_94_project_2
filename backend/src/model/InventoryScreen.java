package model;
import database.PGComms;
import dto.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.ArrayList;

import controller.ManagerController;
import javafx.scene.Scene;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class InventoryScreen {
    
    private StringProperty edible;
    private StringProperty non_edible;

    public StringProperty get_edible(){ return edible; }
    public StringProperty get_non_edible(){ return non_edible; }

    public void set_edible(String n_edible){ edible.set(n_edible); }
    public void set_non_edible(String n_non_edible){ non_edible.set(n_non_edible); }

    private String populate_edible(){
        String edible_query = "SELECT * FROM inv_edible ORDER BY amount_servings;";
        ArrayList<InvEdibleRowDTO> arr_edible = PGComms.issue_query_type_rows_InvEdible(edible_query);

        String output="";
        for(int i=0; i<arr_edible.size(); i++){
            output += print_edible_row(arr_edible.get(i))+"\n";
        }
        return output;
    }

    private String print_edible_row(InvEdibleRowDTO row){
        return ""+row.id_edible()+", "+row.amount_servings()+", "+row.name();
    }


    private String populate_non_edible(){
        String non_edible_query = "SELECT * FROM inv_nonedible ORDER BY amount;";
        ArrayList<InvNonEdibleRowDTO> arr_non_edible = PGComms.issue_query_type_rows_InvNonEdible(non_edible_query);

        String output="";
        for(int i=0; i<arr_non_edible.size(); i++){
            output += print_non_edible_row(arr_non_edible.get(i))+"\n";
        }
        return output;
       // PGComms.issue_query_type_rows_InvNonEdible(string query)

    }

    private String print_non_edible_row(InvNonEdibleRowDTO row){
        return ""+row.id_nonedible()+", "+row.amount()+", "+row.name();
    }


    public AddItemPopUp change_item(ManagerController manager_controller){
        try {
            Stage stage = new Stage();
            FXMLLoader change_item_load = new FXMLLoader(getClass().getResource("/gui/manager/AddItemPopUp.fxml"));
            AddItemPopUp change_item_screen = new AddItemPopUp(stage);
            change_item_load.setController(manager_controller);
            
            Parent root1 = change_item_load.load();
            Scene scene1 = new Scene(root1);


            stage.setScene(scene1);
            stage.show();

            manager_controller.initialize(change_item_screen);
            return change_item_screen;
                
        } catch (Exception e) {
            System.err.println("Error given as: " +e);
        }

        return new AddItemPopUp(null);
        
    }
    

    // @FXML public ItemDetailsPopUp item_click(ManagerController manager_controller, String item_name) {
    //     try{
    //         Stage stage_item = new Stage();
    //         FXMLLoader item_details = new FXMLLoader(getClass().getResource("/gui/manager/ItemDetailsPopUp.fxml"));
    //         ItemDetailsPopUp item_details_model = new ItemDetailsPopUp();
    //         item_details.setController(manager_controller);

    //         Parent root = item_details.load();
    //         Scene scene = new Scene(root);
            
    //         stage_item.setScene(scene);
    //         stage_item.show();
    //         return item_details_model;

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return new ItemDetailsPopUp();
    // }

    public InventoryScreen(){
        
        edible = new SimpleStringProperty("");
        non_edible = new SimpleStringProperty("");

        edible.set(populate_edible());
        non_edible.set(populate_non_edible());

    }



}
