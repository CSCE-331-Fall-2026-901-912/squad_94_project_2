
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.BaseMenu;
import controller.CustomerController;
// import controller.ManagerController;
import database.PGComms;

public class App extends Application {

    
    // @Override

    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader base_menu = new FXMLLoader(getClass().getResource("/gui/cashier/BaseMenu.fxml"));
            CustomerController customer_controller = new CustomerController();
            BaseMenu base_menu_model = new BaseMenu();
            base_menu.setController(customer_controller);
            
            Parent root1 = base_menu.load();
            Scene scene1 = new Scene(root1);

            stage.setScene(scene1);
            stage.show();

            customer_controller.initialize(base_menu_model);
                
        } catch (Exception e) {
            System.err.println("Error given as: " +e);
        }
    }
    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }

        // Identify and establish a reference to the PostgreSQL database.
        PGComms.initialize_database("csce-315-db.engr.tamu.edu", "squad_94_db", "squad_94", args[0]);

        // Ensure the connection works. Determine if it can be opened or closed.
        boolean connection_operational = PGComms.test_connection();

        // If test_connection() returns true, the database can be opened and closed normally. If this is not the case, false is returned.
        if (!connection_operational) {
            return;
        }

        // Launch the application fully (launch JavaFX components).
        launch(args);
    }
}
