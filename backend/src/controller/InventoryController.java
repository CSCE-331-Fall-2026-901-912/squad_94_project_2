package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.Node;
import model.InventoryModel;


/**
 * Controller for viewing the inventory page, changing items, and adding new items to inventory
 *
 * @author Squad 94 - Mairead Finnerty, Asher Blevins
 */
public class InventoryController {
    private final InventoryModel model;
    private final Runnable on_added;

    @FXML private TextArea edible_text_area;
    @FXML private TextArea non_edible_text_area;

    /**
     * Creates controller constructor for syncing the model with this controller.
     *
     * @param model the inventory model used to load the edible and nonedible inventories
     * @param on_added method that allows page to be automatically refreshed so changes show up when items are changed.
     */
    public InventoryController(InventoryModel model, Runnable on_added) {
        this.model = model;
        this.on_added = on_added;
    }

    /**
     * Refreshes the page to ensure updates are shown to the user.
     */
    @FXML private void initialize() {
        refresh();
    }

    /**
     * Ensures the inventory displays are up to date with the latest changes to the database.
     */
    public void refresh() {
        edible_text_area.setText(model.get_edible());
        non_edible_text_area.setText(model.get_non_edible());
        on_added.run();
    }

    /**
     * Handle activation of the AddItemController and transfer to the AddItemPopUp menu.
     * 
     * @param event the event generated when clicking the change item button.
     */
    @FXML private void change_item_button(ActionEvent event) {
        try {
            InventoryModel model = new InventoryModel();
            AddItemController controller = new AddItemController(model, this::refresh);

            Stage stage = ViewLoader.open_window(
                "/gui/manager/AddItemPopUp.fxml",
                controller,
                ((Node) event.getSource()).getScene().getWindow());

            if (stage != null) {
                stage.setTitle("Change Item");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Handle activation of the NewItemController and transfer to the NewItemPopUp menu.
     * 
     * @param event the event generated when clicking the add item button.
     */
    @FXML private void add_item_button(ActionEvent event) {
        try {
            InventoryModel model = new InventoryModel();
            NewItemController controller = new NewItemController(model, this::refresh);

            Stage stage = ViewLoader.open_window(
                "/gui/manager/NewItemPopUp.fxml",
                controller,
                ((Node) event.getSource()).getScene().getWindow());

            if (stage != null) {
                stage.setTitle("New Item");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
