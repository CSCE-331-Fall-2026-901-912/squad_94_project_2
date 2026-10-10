package controller;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

// Keeps all of the code for loading FXML files in one place. 
// This is useful because it allows us to easily change how we load FXML files in the future, and it also makes it easier to handle errors when loading FXML files.
// Since the same format is used to open the files almost everywhere, this class is a good place to put the code for opening FXML files.
public final class ViewLoader {
    private ViewLoader() {}

    /** Loads an FXML file and attaches the given controller. Returns null on failure. */
    public static Parent load(String fxml_path, Object controller) {
        URL url = ViewLoader.class.getResource(fxml_path);
        if (url == null) {
            System.err.println(fxml_path + " not found");
            return null;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);
            return loader.load();
        } catch (IOException e) {
            System.err.println("Could not load " + fxml_path + ": " + e);
            return null;
        }
    }

    /** Opens the FXML file in a new window; owner may be null. */
    public static Stage open_window(String fxml_path, Object controller, Window owner) {
        Parent root = load(fxml_path, controller);
        if (root == null) return null;
        Stage stage = new Stage();
        if (owner != null) {
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
        }
        stage.setScene(new Scene(root));
        stage.show();
        return stage;
    }
}