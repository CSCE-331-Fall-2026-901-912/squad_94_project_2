package model;

import database.PGComms;
import dto.InvEdibleRowDTO;
import dto.JoinMenuDrinksAndInvEdibleRowDTO;
import dto.MenuDrinksRowDTO;
import dto.MenuToppingsRowDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Model class for managing menu-related data, including drinks, toppings, and their ingredients.
 * 
 * @author Squad 94 - Sanjana Ram
 */

public class MenuModel {

    /**
     * Retrieves a list of all drinks from the database.
     * @return a list of MenuDrinksRowDTO objects representing the drinks
     */
    public List<MenuDrinksRowDTO> get_drinks() {
        return PGComms.issue_query_type_rows_MenuDrinks("SELECT * FROM menu_drinks ORDER BY id_drink");
    }

    /**
     * Retrieves a list of all toppings from the database.
     * @return a list of MenuToppingsRowDTO objects representing the toppings
     */
    public List<MenuToppingsRowDTO> get_toppings() {
        return PGComms.issue_query_type_rows_MenuToppings("SELECT * FROM menu_toppings ORDER BY id_topping");
    }

    /**
     * Retrieves a list of all edible items from the inventory.
     * @return a list of InvEdibleRowDTO objects representing the ingredients
     */
    public List<InvEdibleRowDTO> get_ingredients() {
        return PGComms.issue_query_type_rows_InvEdible("SELECT * FROM inv_edible");
    }

    /**
     * Retrieves a list of ingredients associated with a specific drink.
     * @param drink_id the ID of the drink for which to retrieve ingredients
     * @return a list of JoinMenuDrinksAndInvEdibleRowDTO objects representing the ingredients for the specified drink
     */
    public List<JoinMenuDrinksAndInvEdibleRowDTO>
    get_drink_ingredients(int drink_id) {
        return PGComms.issue_query_type_rows_JoinMenuDrinksAndInvEdible(
            "SELECT id_join_menu_drinks_and_inv_edible, " +
            "id_drink, id_edible " +
            "FROM join_menu_drinks_and_inv_edible " +
            "WHERE id_drink = " + drink_id);
    }

    /**
     * Adds a new drink to the menu along with its associated ingredients.
     * @param name the name of the drink
     * @param price the price of the drink
     * @param type the type of drink (eg. milk tea, fruit tea, etc.)
     * @param hot_available whether the drink is available hot
     * @param non_caffeinated whether the drink is non-caffeinated
     * @param ingredient_ids a list of ingredient IDs to associate with the drink
     * @return true if the drink was successfully added, false otherwise
     */
    public boolean add_drink(
            String name,
            BigDecimal price,
            String type,
            boolean hot_available,
            boolean non_caffeinated,
            List<Integer> ingredient_ids) {
        return PGComms.add_drink_with_ingredients(
                name,
                price,
                type,
                hot_available,
                non_caffeinated,
                ingredient_ids);
    }

    /**
     * Updates an existing drink in the menu along with its associated ingredients.
     * @param drink_id the ID of the drink to update
     * @param name the name of the drink 
     * @param price the price of the drink 
     * @param type the type of drink (eg. milk tea, fruit tea, etc.)
     * @param hot_available whether the drink is available hot
     * @param non_caffeinated whether the drink is non-caffeinated
     * @param ingredient_ids a list of ingredient IDs to associate with the drink
     * @return true if the drink was successfully updated, false otherwise
     */
    public boolean update_drink(
            int drink_id,
            String name,
            BigDecimal price,
            String type,
            boolean hot_available,
            boolean non_caffeinated,
            List<Integer> ingredient_ids) {
        return PGComms.update_drink_with_ingredients(
                drink_id,
                name,
                price,
                type,
                hot_available,
                non_caffeinated,
                ingredient_ids);
    }

    /**
     * Deletes a drink from the menu based on its ID.
     * @param drink_id the id of the drink to delete
     * @return true if the drink was successfully deleted, false otherwise
     */
    public boolean delete_drink(int drink_id) {
        return PGComms.delete_drink(drink_id);
    }

    /**
     * Adds a new topping to the menu with the specified name and price.
     * @param name the name of the topping
     * @param price the price of the topping
     * @return true if the topping was successfully added, false otherwise
     */
    public boolean add_topping(String name, BigDecimal price) {
        return PGComms.add_topping_with_inventory(name, price);
    }

    /**
     * Updates an existing topping in the menu with the specified name and price.
     * @param topping_id the ID of the topping to update
     * @param name the new name of the topping
     * @param price the new price of the topping
     * @return true if the topping was successfully updated, false otherwise
     */
    public boolean update_topping(
            int topping_id,
            String name,
            BigDecimal price) {
        return PGComms.update_topping_with_inventory(
                topping_id,
                name,
                price);
    }

    /**
     * Deletes a topping from the menu based on its ID.
     * @param topping_id the ID of the topping to delete
     * @return true if the topping was successfully deleted, false otherwise
     */
    public boolean delete_topping(int topping_id) {
        return PGComms.delete_topping(topping_id);
    }

    /**
     * Retrieves a list of drink names filtered by the specified type.
     * This is used for the cashier side to populate the drink buttons based on the selected type.
     * @param type the type of drinks to retrieve (e.g., "milk tea", "fruit tea")
     * @return a list of drink names matching the specified type
     */
    public List<String> get_drink_names_by_type(String type) {
        return PGComms.get_drink_names_by_type(type);
    }

    /**
     * Retrieves a list of sold-out drink names filtered by the specified type.
     * This is used for the cashier side to display sold-out drinks based on the selected type
     * @param type the type of drinks to retrieve (e.g., "milk tea", "fruit tea")
     * @return a list of sold-out drink names matching the specified type
     */
    public List<String> get_sold_out_drink_names_by_type(String type) {
        return PGComms.get_sold_out_drink_names_by_type(type);
    }

    /**
     * Retrieves a list of all topping names from the database. This is used for the cashier side to populate the topping buttons.
     * @return a list of topping names
     */
    public List<String> get_topping_names() {
        return PGComms.get_topping_names();
    }

    /**
     * Checks if a specific drink is available in hot form.
     * This is used for the cashier side to determine if the hot option should be displayed for a drink.
     * @param drink_name the name of the drink to check
     * @return true if the drink is available hot, false otherwise
     */

    public boolean is_hot_available(String drink_name) {
        return PGComms.is_hot_available(drink_name);
    }

    /**
     * Retrieves the price of a specific drink from the database. This is used for the cashier side to display the correct price for a drink.
     * @param drink_name the name of the drink for which to retrieve the price
     * @return the price of the specified drink as a BigDecimal
     */
    public BigDecimal get_drink_price(String drink_name) {
        return PGComms.get_drink_price(drink_name);
    }

    /**
     * Retrieves the price of a specific topping from the database. This is used for the cashier side to display the correct price for a topping.
     * @param topping_name the name of the topping for which to retrieve the price
     * @return the price of the specified topping as a BigDecimal
     */
    public BigDecimal get_topping_price(String topping_name) {
        return PGComms.get_topping_price(topping_name);
    }
}