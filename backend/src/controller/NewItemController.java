package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import database.PGComms;

import javafx.event.ActionEvent;

import dto.InvEdibleRowDTO;
import dto.InvNonEdibleRowDTO;
import model.InventoryModel;
import java.util.List;

public class NewItemController {
    private final InventoryModel model;
    private final Runnable on_saved;

    @FXML private TextField input_new_item_name;
    @FXML private TextField input_new_item_quantity;
    @FXML private TextField input_new_item_type;

    public NewItemController(InventoryModel model, Runnable on_saved) {
        this.model = model;
        this.on_saved = on_saved;
    }

    // Handle visual warnings displayed if submit new item button is pressed and some user input is invalid and adds new nonedible and edible items to inventory.
    // Otherwise, simply close the add item (newitempopup) page to indicate success to the user.
    @FXML public void submit_new_item_button(ActionEvent event) {
        String name = input_new_item_name.getText().trim();
        String text = input_new_item_quantity.getText().trim();
        String type = input_new_item_type.getText().trim();

        if (name.isEmpty() || text.isEmpty() || type.isEmpty()) {
            show_warning("Name, type, and quantity are required.");
            return;
        }

       

        if (type.toLowerCase().equals("edible") && type.toLowerCase().equals("nonedible")) {
            show_warning("Type is incorrect. Try Edible or Nonedible.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            show_warning("Quantity must be a valid number.");
            return;
        }       

        List<InvNonEdibleRowDTO> arr_non_edible = model.find_non_edible(name);
        List<InvEdibleRowDTO> arr_edible = model.find_edible(name);

        if (!arr_non_edible.isEmpty() || !arr_edible.isEmpty()) {
            show_warning("Item \"" + name + "\" is already in inventory.");
            return;
        }
        

        try {
            if(type.toLowerCase().equals("edible")){
                
                if (!PGComms.add_edible_item_to_inventory(name, quantity)) {
                    show_warning("The edible inventory item could not be added.");
                    return;
                }
            }
            else{
                if (!PGComms.add_nonedible_item_to_inventory(name, quantity)) {
                    show_warning("The nonedible inventory item could not be added.");
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

    private void show_warning(String message) {
        new Alert(
            Alert.AlertType.WARNING,
            message
        ).showAndWait();
    }
}
