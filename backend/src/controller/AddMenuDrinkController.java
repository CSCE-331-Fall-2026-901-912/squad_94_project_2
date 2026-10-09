package controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.MenuModel;
import dto.InvEdibleRowDTO;
import dto.MenuDrinksRowDTO;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

public class AddMenuDrinkController {
    private static final int MAX_INGREDIENTS = 8;
    private static final String SELECTED_STYLE =
        "-fx-background-color: #2e9e5b; -fx-text-fill: white; -fx-font-weight: bold;";

    @FXML private Label title_label;
    @FXML private TextField name_field;
    @FXML private TextField price_field;
    @FXML private ComboBox<String> type_box;
    @FXML private RadioButton hot_button;
    @FXML private RadioButton caff_button;
    @FXML private Button submit_button;
    @FXML private Button delete_button;
    @FXML private GridPane ingredient_grid;

    private final MenuModel model;
    private final Runnable on_added;  
    private final MenuDrinksRowDTO existing;                  
    private final Map<Button, Integer> selected_ingredients = new HashMap<>();

    public AddMenuDrinkController(MenuModel model, Runnable on_added, MenuDrinksRowDTO existing){
        this.model = model;
        this.on_added = on_added;
        this.existing = existing;
    }

    @FXML
    private void initialize() {
        type_box.setItems(FXCollections.observableArrayList(
                "milk tea", "fresh tea", "fruit tea", "no caff tea"));
        if (existing != null) {
            title_label.setText("Edit Drink");
            submit_button.setText("Save");
            delete_button.setVisible(true);
            delete_button.setManaged(true);
            name_field.setText(existing.name());
            price_field.setText(existing.price().toPlainString());
            type_box.setValue(existing.type());
            hot_button.setSelected(existing.hot_available());
            caff_button.setSelected(existing.is_non_caffeinated());
        }

        ingredient_grid.setHgap(8);
        ingredient_grid.setVgap(8);

        int column = 0;
        int row = 0;
        for (InvEdibleRowDTO ingredient : model.get_ingredients()) {
            Button ingredient_button = new Button(ingredient.name());
            ingredient_button.setMaxWidth(Double.MAX_VALUE);
            ingredient_button.setUserData(ingredient.id_edible());
            ingredient_button.setOnAction(event -> toggle_ingredient(ingredient_button));
            ingredient_grid.add(ingredient_button, column, row);

            column++;
            if (column == 2) {
                column = 0;
                row++;
            }
        }

        if (existing != null) {
            List<Integer> ingredient_ids =
                model.get_drink_ingredients(existing.id_drink())
                .stream()
                .map(row_item -> row_item.id_edible())
                .toList();

            for (Node node : ingredient_grid.getChildren()) {
                Button ingredient_button = (Button) node;
                int ingredient_id = (Integer) ingredient_button.getUserData();
                if (ingredient_ids.contains(ingredient_id)) {
                    selected_ingredients.put(ingredient_button, ingredient_id);
                }
            }
            refresh_ingredient_buttons();
        }
    }

    private void toggle_ingredient(Button ingredient_button) {
        if (selected_ingredients.containsKey(ingredient_button)) {
            selected_ingredients.remove(ingredient_button);
        } else if (selected_ingredients.size() < MAX_INGREDIENTS) {
            selected_ingredients.put(ingredient_button, (Integer) ingredient_button.getUserData());
        }

        refresh_ingredient_buttons();
    }

    private void refresh_ingredient_buttons() {
        boolean max_selected = selected_ingredients.size() == MAX_INGREDIENTS;

        for (javafx.scene.Node node : ingredient_grid.getChildren()) {
            Button ingredient_button = (Button) node;
            boolean selected = selected_ingredients.containsKey(ingredient_button);

            ingredient_button.setStyle(selected ? SELECTED_STYLE : "");
            ingredient_button.setDisable(max_selected && !selected);
        }
    }

    @FXML
    private void submit() {
        String name = name_field.getText().trim();
        String type = type_box.getValue();

        if (name.isEmpty() || type == null || price_field.getText().trim().isEmpty()) {
            show_error("Name, price, and type are required.");
            return;
        }

        BigDecimal price;
        try {
            price = new BigDecimal(price_field.getText().trim());
        } catch (NumberFormatException e) {
            show_error("Price must be a valid number.");
            return;
        }

        boolean saved = existing == null
            ? model.add_drink(
                name, price, type, hot_button.isSelected(),
                caff_button.isSelected(), selected_ingredients.values().stream().toList())
            : model.update_drink(
                existing.id_drink(), name, price, type, hot_button.isSelected(),
                caff_button.isSelected(), selected_ingredients.values().stream().toList());
        if (!saved) {
            show_error("The drink could not be saved.");
            return;
        }

        on_added.run();
        ((Node) submit_button).getScene().getWindow().hide();
    }

    @FXML
    private void delete() {
        if (existing == null) {
            return;
        }

        Alert confirmation = new Alert(
            Alert.AlertType.CONFIRMATION,
            "Delete " + existing.name() + "? This action cannot be undone.",
            ButtonType.CANCEL,
            ButtonType.OK
        );
        confirmation.setTitle("Delete Drink");
        confirmation.setHeaderText(null);

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        if (!model.delete_drink(existing.id_drink())) {
            show_error("The drink could not be deleted.");
            return;
        }

        on_added.run();
        ((Node) submit_button).getScene().getWindow().hide();
    }

    private void show_error(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}
