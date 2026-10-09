package model;

import database.PGComms;
import dto.InvEdibleRowDTO;
import dto.InvNonEdibleRowDTO;

import java.util.List;

public class InventoryModel {
    public String get_edible() {
        List<InvEdibleRowDTO> edibles = PGComms.issue_query_type_rows_InvEdible("SELECT * FROM inv_edible ORDER BY amount_servings");
        String output = "";
        for (InvEdibleRowDTO row : edibles) {
            output += row.id_edible() + ", " + row.amount_servings() + ", " + row.name() + "\n";
        }
        return output;
    }

    public String get_non_edible() {
        List<InvNonEdibleRowDTO> non_edibles = PGComms.issue_query_type_rows_InvNonEdible("SELECT * FROM inv_nonedible ORDER BY amount");
        String output = "";
        for (InvNonEdibleRowDTO row : non_edibles) {
            output += row.id_nonedible() + ", " + row.amount() + ", " + row.name() + "\n";
        }
        return output;
    }

    public boolean change_edible_quantity(
            int edible_id,
            int quantity) {
        return PGComms.modify_topping_quantity_in_edible_inventory(
            edible_id,
            quantity);
    }

    public boolean change_non_edible_quantity(
            int non_edible_id,
            int quantity) {
        return PGComms.modify_topping_quantity_in_nonedible_inventory(
            non_edible_id,
            quantity);
    }

    public List<InvEdibleRowDTO> find_edible(String name) {
        return PGComms.issue_query_type_rows_InvEdible("SELECT * FROM inv_edible WHERE name ILIKE '" + name + "';");
    }

    public List<InvNonEdibleRowDTO> find_non_edible(String name) {
        return PGComms.issue_query_type_rows_InvNonEdible("SELECT * FROM inv_nonedible WHERE name ILIKE '" + name + "';");
    }
}