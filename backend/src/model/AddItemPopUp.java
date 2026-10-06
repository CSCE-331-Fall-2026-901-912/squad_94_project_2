package model;

import javafx.stage.Stage;

public class AddItemPopUp {
    private Stage stage_add_item;


    public void submit_changes(){
        stage_add_item.close();
    }

    public void add_item(Stage stage_add_item){
        this.stage_add_item = stage_add_item;
    }
}
