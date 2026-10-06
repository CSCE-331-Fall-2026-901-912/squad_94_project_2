package controller;

import model.BaseMenu;
import model.CancellationPopUp;
import model.OrderMenu;
import model.TipPopUp;

import dto.InvEdibleRowDTO;
import dto.InvNonEdibleRowDTO;
import dto.EmployeesRowDTO;
import dto.MenuDrinksRowDTO;
import dto.MenuToppingsRowDTO;
import dto.JoinMenuDrinksAndInvEdibleRowDTO;
import dto.JoinMenuToppingsAndInvEdibleRowDTO;
import dto.OrdersRowDTO;

import javafx.fxml.FXML;
// import javafx.scene.control.Button;
// import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
// import javafx.scene.text.Text;

public class CustomerController {

    
    private BaseMenu base_menu;
    private CancellationPopUp cancel_pop_up;
    private OrderMenu order_menu;
    private TipPopUp tip_pop_up;


    // BASEMENU TEXTS & BUTTONS
    @FXML private TextArea current_order;
    @FXML private Label order_total;
    
    @FXML public void add_drink(){
        order_menu = base_menu.add_drink(this);
    }
    @FXML public void clear_order(){
        cancel_pop_up = base_menu.clear_order(this);
    }
    @FXML public void finish_order(){
        tip_pop_up = base_menu.finish_order(this);
    }
    @FXML public void manager_view(){
        base_menu.manager_view(this);
    }

    // CANCELLATION POP UP BUTTONS
    @FXML public void confirm_cancel(){
        cancel_pop_up.confirm_cancel();
        // TODO: clear current_order
    }
    @FXML public void deny_cancel(){
        cancel_pop_up.deny_cancel();
        // TODO: close popup
    }

    // ORDER MENU BUTTONS
    // TODO: add buttons

    // TIP POP UP BUTTONS
    @FXML private Label tip_total;

    @FXML public void tip0(){
        tip_pop_up.tip0();
    }
    @FXML public void tip10(){
        tip_pop_up.tip10();
    }
    @FXML public void tip15(){
        tip_pop_up.tip15();
    }
    @FXML public void tip20(){
        tip_pop_up.tip20();
    }
    @FXML public void tip25(){
        tip_pop_up.tip25();
    }
    @FXML public void tipdone(){
        tip_pop_up.tipdone();
        // TODO: update PSQL database, reset current_order data 
        // TODO: close window
    }


    public void initialize(BaseMenu base_menu){
        this.base_menu = base_menu;
        current_order.textProperty().bind(base_menu.get_current_order());
        
    }
}
