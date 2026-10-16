package controller;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Keeps all the code for loading FXML files in one place.
 * This is useful because it allows us to easily change how we load FXML files in the future, and it also makes it easier to handle errors when loading FXML files.
 * 
 * @author Squad 94 - Sanjana Ram
 */
public final class ViewLoader {
    /** 
     * Private constructor to prevent instantiation. 
     */
    private ViewLoader() {}

    /** 
     * Loads an FXML file and attaches the given controller. Returns null on failure. 
     * @param fxml_path the path to the FXML file, relative to the classpath
     * @param controller the controller to attach to the FXML file
     * @return the root node of the loaded FXML file, or null if loading failed
     */
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

    /** 
     * Opens the FXML file in a new window; owner may be null.
     * @param fxml_path the path to the FXML file, relative to the classpath
     * @param controller the controller to attach to the FXML file
     * @param owner the window that owns the new window, or null if there is no owner
     * @return the new stage, or null if loading failed
     */
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