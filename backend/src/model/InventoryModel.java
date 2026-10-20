package model;

import database.PGComms;
import dto.IDNameValDTO;
import dto.InvEdibleRowDTO;
import dto.InvNonEdibleRowDTO;

import java.util.List;

/**
 * Model class for managing inventory related data, including edible and nonedible items.
 * 
 * @author Squad 94 - Mairead Finnerty
 */
public class InventoryModel {

    
    /**
     * Get all edible items from the inv_edible table as one string. Order items by their amount of servings constituted in the inventory (least to greatest).
     * @return a string of all the items in the edible inventory.
     */
    public String get_edible() {
        List<InvEdibleRowDTO> edibles = PGComms.issue_query_type_rows_InvEdible("SELECT * FROM inv_edible ORDER BY amount_servings");
        String output = "";
        for (InvEdibleRowDTO row : edibles) {
            output += row.id_edible() + ", " + row.amount_servings() + ", " + row.name() + "\n";
        }
        return output;
    }

    
    /**
     * Get all non-edible items from the inv_nonedible table as one string. Order items by their amount in the inventory (least to greatest).
     * @return a string of all the items in the nonedible inventory.
     */
    public String get_non_edible() {
        List<InvNonEdibleRowDTO> non_edibles = PGComms.issue_query_type_rows_InvNonEdible("SELECT * FROM inv_nonedible ORDER BY amount");
        String output = "";
        for (InvNonEdibleRowDTO row : non_edibles) {
            output += row.id_nonedible() + ", " + row.amount() + ", " + row.name() + "\n";
        }
        return output;
    }

    /**
     * Updates the quantity of the specified edible item.
     * @return a boolean that's true if the quantity was successfully updated and false if not.
     * @param edible_id the id of the edible inventory item.
     * @param quantity the quantity of the edible inventory item.
     */
    public boolean change_edible_quantity(
            int edible_id,
            int quantity) {
        return PGComms.modify_topping_quantity_in_edible_inventory(
            edible_id,
            quantity);
    }

    /**
     * Updates the quantity of the specified nonedible item.
     * @return a boolean that's true if the quantity was successfully updated and false if not.
     * @param non_edible_id the id of the nonedible inventory item.
     * @param quantity the quantity of the nonedible inventory item.
     */
    public boolean change_non_edible_quantity(
            int non_edible_id,
            int quantity) {
        return PGComms.modify_topping_quantity_in_nonedible_inventory(
            non_edible_id,
            quantity);
    }

    /**
     * Find a specific item row in the edible inventory database table
     * @return a list of edible inventory rows
     * @param name the name of the item
     */
    public List<InvEdibleRowDTO> find_edible(String name) {
        return PGComms.issue_query_type_rows_InvEdible("SELECT * FROM inv_edible WHERE name ILIKE '" + name + "';");
    }

    /**
     * Find a specific item row in the nonedible inventory database table
     * @return a list of nonedible inventory rows
     * @param name the name of the item
     */
    public List<InvNonEdibleRowDTO> find_non_edible(String name) {
        return PGComms.issue_query_type_rows_InvNonEdible("SELECT * FROM inv_nonedible WHERE name ILIKE '" + name + "';");
    }

    /**
     * Grab the list of out of stock items.
     * @return list of items with each entry detailing the item ID, name, and amount in stock
     */
    public List<IDNameValDTO> get_out_of_stock() {
        return PGComms.get_out_of_stock();
    }

    /**
     * Grab the list of running low inventory.
     * @return list of items with each entry detailing the item ID, name, and amount in stock
     */
    public List<IDNameValDTO> get_running_low() {
        return PGComms.get_running_low_on();
    }
}