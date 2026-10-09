package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.Node;
import model.InventoryModel;

public class InventoryController {
    private final InventoryModel model;

    @FXML private TextArea edible_text_area;
    @FXML private TextArea non_edible_text_area;

    public InventoryController(InventoryModel model) {
        this.model = model;
    }

    @FXML private void initialize() {
        refresh();
    }
    
    public void refresh() {
        edible_text_area.setText(model.get_edible());
        non_edible_text_area.setText(model.get_non_edible());
    }

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
}
