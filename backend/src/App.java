import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.CurrentOrderModel;
import model.MenuModel;
import controller.BaseMenuController;
import database.PGComms;

public class App extends Application {

    public void start(Stage stage) throws Exception {
        CurrentOrderModel order_model = new CurrentOrderModel();
        MenuModel menu_model = new MenuModel();

        BaseMenuController controller =
            new BaseMenuController(order_model, menu_model);

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/gui/cashier/BaseMenu.fxml"));
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }

        // Identify and establish a reference to the PostgreSQL database.
        PGComms.initialize_database("csce-315-db.engr.tamu.edu", "squad_94_db", "squad_94", args[0]);

        // Test if the database can be opened and closed normally. Terminate the program if this is not the case.
        if (!PGComms.test_connection()) {
            return;
        }

        // Launch the application fully (launch JavaFX components).
        launch(args);
    }
}
