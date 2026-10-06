package model;

import javafx.stage.Stage;

public class CancellationPopUp {

    private Stage stage_cancel;

    // GETTERS

    // SETTERS
    public void confirm_cancel(){
        // TODO: update PSQL database, reset current_order data
        stage_cancel.close();
    }
    public void deny_cancel(){
        stage_cancel.close();
    }

    // CONSTRUCTOR
    public CancellationPopUp(Stage stage_cancel){
        this.stage_cancel = stage_cancel;
    }
}
