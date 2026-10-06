package model;

import database.PGComms;
import dto.InvEdibleRowDTO;
import dto.MenuDrinksRowDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.RadioButton;

public class AddMenuDrink {
    @FXML private Label titleLabel;
    @FXML private TextField name_field;
    @FXML private TextField price_field;
    @FXML private TextField type_field;
    @FXML private RadioButton hot_available_radio;
    @FXML private RadioButton non_caffeinated_radio;
    @FXML private Button submitButton;
    @FXML private GridPane ingredient_grid;

    private final Runnable on_added;  
    private final MenuDrinksRowDTO existing;                  

    public AddMenuDrink(Runnable on_added) {
        this.on_added = on_added;
        this.existing = null;
    }

    public AddMenuDrink(Runnable on_added, MenuDrinksRowDTO existing) {
        this.on_added = on_added;
        this.existing = existing;
    }

    @FXML
    private void initialize() {
        ingredient_grid.setHgap(8);
        ingredient_grid.setVgap(8);

        int column = 0;
        int row = 0;
        for (InvEdibleRowDTO ingredient : PGComms.issue_query_type_rows_InvEdible(
                "SELECT id_edible, name, amount_servings "
                + "FROM inv_edible ORDER BY name")) {
            Button ingredient_button = new Button(ingredient.name());
            ingredient_button.setMaxWidth(Double.MAX_VALUE);
            ingredient_grid.add(ingredient_button, column, row);

            column++;
            if (column == 2) {
                column = 0;
                row++;
            }
        }
    }

}
