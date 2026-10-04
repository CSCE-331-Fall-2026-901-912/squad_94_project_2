
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.BaseMenu;
import controller.CustomerController;
// import controller.ManagerController;

public class App extends Application {

    
    // @Override

    public void start(Stage stage) throws Exception {
        try {
            // Stage stage_cancel = new Stage();
            // Stage stage_order = new Stage();
            // Stage stage_tip = new Stage();

            FXMLLoader base_menu = new FXMLLoader(getClass().getResource("/gui/cashier/BaseMenu.fxml"));
            // FXMLLoader cancellation_pop_up = new FXMLLoader(getClass().getResource("/gui/cashier/BaseMenu.fxml"));
            // FXMLLoader order_menu = new FXMLLoader(getClass().getResource("/gui/cashier/OrderMenu.fxml"));
            // FXMLLoader tip_pop_up = new FXMLLoader(getClass().getResource("/gui/cashier/TipPopUp.fxml"));
            
            CustomerController customer_controller = new CustomerController();
            // ManagerController manager_controller = new ManagerController();

            BaseMenu base_menu_model = new BaseMenu();
            // CancellationPopUp cancellation_pop_up_model = new CancellationPopUp();
            // OrderMenu order_menu_model = new OrderMenu();
            // TipPopUp tip_pop_up_model = new TipPopUp(); 

            base_menu.setController(customer_controller);
            // cancellation_pop_up.setController(customer_controller);
            
            Parent root1 = base_menu.load();
            // Parent root2 = cancellation_pop_up.load();
            // Parent root3 = order_menu.load();
            // Parent root4 = tip_pop_up.load();

            Scene scene1 = new Scene(root1);
            // Scene scene2 = new Scene(root2);
            // Scene scene3 = new Scene(root3);
            // Scene scene4 = new Scene(root4);

            stage.setScene(scene1);
            // stage2.setScene(scene2);
            stage.show();
            // stage2.show();

            customer_controller.initialize(base_menu_model);
                
        } catch (Exception e) {
            System.err.println("Error given as: " +e);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
