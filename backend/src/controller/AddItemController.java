package controller;



import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

import dto.InvEdibleRowDTO;
import dto.InvNonEdibleRowDTO;
import model.InventoryModel;
import java.util.List;

/**
 * Controller for user to change quantities of items
 *
 * @author Squad 94 - Mairead Finnerty, Asher Blevins
 */
public class AddItemController {
    private final InventoryModel model;
    private final Runnable on_saved;

    @FXML
    private TextField input_name;
    @FXML
    private TextField input_quantity;

    /**
     * Creates controller constructor for syncing the model with this controller.
     *
     * @param model the inventory model used to look up the items and update their quantities
     * @param on_saved method that allows page to be automatically refreshed so changes show up when items are changed.
     */
    public AddItemController(InventoryModel model, Runnable on_saved) {
        this.model = model;
        this.on_saved = on_saved;
    }

    
    /**
     * Handle visual warnings displayed if submit changes button is pressed and some user input is invalid.
     * Otherwise, simply close the change item pop up page to indicate success to the user.
     *
     * @param event the event generated when clicking the submit changes button.
     */
    @FXML
    public void submit_changes_button(ActionEvent event) {
        String name = input_name.getText().trim();
        String text = input_quantity.getText().trim();

        if (name.isEmpty() || text.isEmpty()) {
            show_warning("Name and quantity are required.");
            return;
        }
        List<InvNonEdibleRowDTO> arr_non_edible = model.find_non_edible(name);
        List<InvEdibleRowDTO> arr_edible = model.find_edible(name);

        if (arr_non_edible.isEmpty() && arr_edible.isEmpty()) {
            show_warning("No inventory item named \"" + name + "\" was found.");
            return;
        }

        if (!arr_non_edible.isEmpty() && !arr_edible.isEmpty()) {
            show_warning("More than one inventory item matched \"" + name + "\".");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            show_warning("Quantity must be a valid number.");
            return;
        }

        try {
            if(!arr_non_edible.isEmpty()){
                int non_edible_id = arr_non_edible.getFirst().id_nonedible();
                if (!model.change_non_edible_quantity(non_edible_id, quantity)) {
                    show_warning("The non-edible inventory item could not be updated.");
                    return;
                }
            }
            else{
                int edible_id = arr_edible.getFirst().id_edible();
                if (!model.change_edible_quantity(edible_id, quantity)) {
                    show_warning("The edible inventory item could not be updated.");
                    return;
                }
            }
        } catch (Exception e) {
            show_warning("Error updating item: " + e.getMessage());
            return;
        }

        Node source = (Node) event.getSource();
        source.getScene().getWindow().hide();
        on_saved.run();
    }

    /**
     * Displays a warning message to the user 
     *
     * @param message warning message specific to the instance that changes based on where the warning was thrown.
     */
    private void show_warning(String message) {
        new Alert(
            Alert.AlertType.WARNING,
            message
        ).showAndWait();
    }
}
