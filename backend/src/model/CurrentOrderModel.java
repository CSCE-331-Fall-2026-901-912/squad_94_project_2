package model;

import dto.MenuDrinksRowDTO;
import dto.MenuToppingsRowDTO;
import dto.OrderLineDTO;
import dto.OrdersRowDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import database.PGComms;

// Model class for managing current order-related data, including drinks, toppings, associated prices, IDs, and checkout procedures.

/**
 * Model class for managing the current order, including drinks, toppings, associated prices, IDs, and checkout procedures.
 * This class provides methods for adding items to the order, clearing the order, refreshing the display of the current order and total price, and handling the checkout process, including updating inventory quantities and inserting order details into the database.
 * It ensures that the current order is accurately represented and that all necessary database operations are performed to finalize the order.
 * 
 * @author Squad 94 - Asher Blevins
 */
public class CurrentOrderModel {

    // List to hold the current order lines, each representing an item in the order.
    private final List<OrderLineDTO> lines = new ArrayList<>();

    // StringProperty to hold the current order details for display purposes.
    private final StringProperty current_order = new SimpleStringProperty("Item1: ");

    // StringProperty to hold the total price of the current order for display purposes.
    private final StringProperty order_total = new SimpleStringProperty("Total: 0.00");

    // Getters for the current order and total price properties, allowing other parts of the application to bind to these properties for UI updates.

    /**
     * Returns the StringProperty representing the current order details for display purposes.
     * This property can be bound to UI elements to automatically update the display when the current order changes.
     * @return The StringProperty representing the current order details.
     */
    public StringProperty get_current_order() {
        return current_order;
    }

    /**
     * Returns the StringProperty representing the total price of the current order for display purposes.
     * This property can be bound to UI elements to automatically update the display when the total price changes.
     * @return The StringProperty representing the total price of the current order.
     */
    public StringProperty get_order_total() {
        return order_total;
    }

    /**
     * Returns a list of OrderLineDTO objects representing the current order lines.
     * This method provides method to allow other parts of the application to access the order details comprehensively.
     * @return A copy of the list of OrderLineDTO objects representing the current order lines.
     */
    public List<OrderLineDTO> get_lines() {
        return List.copyOf(lines);
    }

    /**
     * Calculates and returns the total price of the current order by summing the prices of all order lines.
     * This method iterates through the list of order lines, retrieves the price of each line, and accumulates the total using BigDecimal for accurate monetary calculations.
     * The total price is returned as a BigDecimal, allowing for precise representation of currency values.
     * @return A BigDecimal representing the total price of the current order.
     */
    public BigDecimal get_total() {
        return lines.stream()
            .map(OrderLineDTO::price)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Add a new item to the current order and refresh the display to reflect the updated order details and total price.

    /**
     * Adds a new item to the current order and refreshes the display to reflect the updated order details and total price.
     * This method takes an OrderLineDTO object representing the item to be added, appends it to the list of order lines,
     * and then calls the refresh_display() method to update the current order and total price properties for UI display.
     * @param line The OrderLineDTO object representing the item to be added to the current order.
     */
    public void add_item(OrderLineDTO line) {
        lines.add(line);
        refresh_display();
    }

    /**
     * Clears the current order by removing all items from the order lines list and refreshing the display to show an empty order and a total price of zero.
     * This method is typically called when the user confirms the cancellation of an order or when the order is completed and needs to be reset for a new transaction.
     * This method also calls refresh_display() to update the current order and total price properties for UI display, ensuring that the user sees an accurate representation of the cleared order.
     */
    public void clear() {
        checkout(BigDecimal.ZERO, true);
        lines.clear();
        refresh_display();
    }

    // Refresh the display of the current order and total price based on the items in the order.

    /**
     * Refreshes the display of the current order and total price based on the items in the order.
     * This method constructs a string representation of the current order, including item details and total price, and updates the current_order and order_total properties for UI display.
     * If the order is empty, it sets the current_order property to "Item1: " and the order_total property to "Total: 0.00".
     */
    public void refresh_display() {
        if (lines.isEmpty()) {
            current_order.set("Item1: ");
            order_total.set("Total: 0.00");
            return;
        }

        // Build a string representation of the current order, including item details and total price.
        // This goes on the current_order StringProperty for display in the UI.
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < lines.size(); i++) {
            OrderLineDTO line = lines.get(i);

            if (i > 0) {
                text.append('\n');
            }

            text.append("Item ")
                .append(i + 1)
                .append(": ")
                .append(line.drink_name())
                .append(", ")
                .append(line.topping1_name())
                .append(", ")
                .append(line.topping2_name())
                .append(", ")
                .append(line.ice_level())
                .append(", ")
                .append(line.sugar_percent())
                .append("% : ")
                .append(line.price());
        }

        current_order.set(text.toString());
        order_total.set("Total: " + get_total());
    }

    // Get the last order ID from the database to determine the next order ID for a new order. 
    // This is used during checkout to ensure that each order has a unique identifier.

    /**
     * Retrieves the last order ID from the database to determine the next order ID for a new order.
     * @return The last order ID as an integer, which can be used to generate the next unique order ID for a new order.
     */
    private int get_last_orderID(){
        ArrayList<OrdersRowDTO> order_rows;
        order_rows = PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order DESC LIMIT 1;");
        
        int last_num = order_rows.getFirst().id_order();
        return last_num;
    }
    
    // Find a drink by its name in the menu database and return its corresponding MenuDrinksRowDTO.

    /**
     * Finds a drink by its name in the menu database and returns its corresponding MenuDrinksRowDTO.
     * This method queries the database for drinks matching the provided name and returns the first matching result.
     * If no drinks are found, it returns null. If multiple drinks are found with the same name, it throws an IllegalStateException.
     * @param name The name of the drink to be searched in the menu database.
     * @return The MenuDrinksRowDTO corresponding to the found drink, or null if no drink is found.
     * @throws IllegalStateException if multiple drinks are found with the same name.
     */
    private MenuDrinksRowDTO find_drink(String name) {
        List<MenuDrinksRowDTO> drinks =
            PGComms.issue_query_type_rows_MenuDrinks(
                "SELECT * FROM menu_drinks WHERE name = '" + name + "'");

        if (drinks.isEmpty()) {
            return null;
        }

        if (drinks.size() > 1) {
            throw new IllegalStateException(
                "Multiple drinks have the name: " + name);
        }

        return drinks.get(0);
    }

    // Find a topping by its name in the topping menu database and return its corresponding MenuToppingsRowDTO.

    /**
     * Finds a topping by its name in the topping menu database and returns its corresponding MenuToppingsRowDTO.
     * This method queries the database for toppings matching the provided name and returns the first matching result.
     * If no toppings are found, it returns null. If multiple toppings are found with the same name, it throws an IllegalStateException.
     * @param name The name of the topping to be searched in the topping menu database.
     * @return The MenuToppingsRowDTO corresponding to the found topping, or null if no topping is found.
     * @throws IllegalStateException if multiple toppings are found with the same name.
     */
    private MenuToppingsRowDTO find_topping(String name) {
        List<MenuToppingsRowDTO> toppings =
            PGComms.issue_query_type_rows_MenuToppings(
                "SELECT * FROM menu_toppings WHERE name = '" + name + "'");

        if (toppings.isEmpty()) {
            return null;
        }

        if (toppings.size() > 1) {
            throw new IllegalStateException(
                "Multiple toppings have the name: " + name);
        }

        return toppings.get(0);
    }

    // Decrement the inventory quantities of the ingredients used in a drink when an order is placed using information from the join table that links drinks to their ingredients. This ensures that the inventory reflects the consumption of ingredients for each order.

    /**
     * Decrements the inventory quantities of the ingredients used in a drink when an order is placed using information from the join table that links drinks to their ingredients.
     * This method retrieves the list of edible ingredient IDs associated with the specified drink ID and decrements their quantities in the edible inventory. 
     * If any ingredient cannot be updated, an IllegalStateException is thrown to indicate a failure in updating the inventory.
     * @param drink_id The ID of the drink for which the ingredient quantities need to be decremented in the inventory.
     * @throws IllegalStateException if any ingredient cannot be updated in the inventory.
     */
    private void decrement_drink_ingredients(int drink_id) {
        List<Integer> edible_ids =
            PGComms.get_id_edibles_for_drink(drink_id);

        for (int edible_id : edible_ids) {
            if (!PGComms.modify_topping_quantity_in_edible_inventory(
                    edible_id,
                    -1)) {
                throw new IllegalStateException(
                    "Could not update drink ingredient inventory.");
            }
        }
    }

    // Checkout the current order by inserting each order line into the database, updating inventory quantities for drinks and toppings, and applying a tip rate to the total price. 
    // This method ensures that all necessary database operations are performed to finalize the order.

    /**
     * Checks out the current order by inserting each order line into the database, updating inventory quantities for drinks and toppings, and applying a tip rate to the total price.
     * This method ensures that all necessary database operations are performed to finalize the order. It handles both normal checkout and special cases where the order is discarded without consuming ingredients.
     * If the order is empty, or required non-edible items are unavailable, an IllegalStateException is thrown.
     * 
     * @param tip_rate The tip rate to be applied to the total price of the order, represented as a BigDecimal.
     * @param discard A boolean flag indicating whether the order is being discarded (true) or completed (false). If true, the order will be logged with a "discard" status and no ingredients will be consumed.
     * @return True if the checkout process is successful, indicating that all order lines were processed and inserted into the database without errors.
     * @throws IllegalStateException if the order is empty, required non-edible items are unavailable, or if any inventory updates fail during the checkout process.
     */
    public boolean checkout(BigDecimal tip_rate, boolean discard) {

        // If discard=true, this is a special case. We say an order that was about to be considered completed is discarded.
        // The order will still be logged, but will have the status discard. Additionally, no ingredients will be consumed.

        // Ensure that the order is not empty before proceeding with checkout.
        if(lines.isEmpty()) {
            if(discard) {
                throw new IllegalStateException("Cannot discard an empty order.");
            }
            else {
                throw new IllegalStateException("Cannot checkout an empty order.");
            }
        }

        if (!PGComms.are_required_nonedible_items_available(lines.size())) {
            throw new IllegalStateException(
                "Cannot complete the order because one or more required non-edible inventory items (IDs 1-4) do not have enough stock.");
        }

        for(OrderLineDTO line : lines) {
            // Calculate tips individually to make it easier to insert each drink as its own line
            double price_with_tip = line.price().multiply(BigDecimal.ONE.add(tip_rate)).setScale(2, RoundingMode.HALF_UP).doubleValue();
            int next_order_id = get_last_orderID() + 1;
            int id_drink = find_drink(line.drink_name()).id_drink();
            
            int topping1_id = 0;
            int topping2_id = 0;

            if (!discard) {
                decrement_drink_ingredients(id_drink);
            }

            // Decrement the inventory quantities for the toppings used in the order, if they are not "No topping".
            // This ensures that the inventory accurately reflects the consumption of toppings for each order.
            if (!line.topping1_name().equals("No topping")) {
                MenuToppingsRowDTO topping1 =
                    find_topping(line.topping1_name());

                if (topping1 == null) {
                    throw new IllegalStateException(
                        "Topping not found: " + line.topping1_name());
                }
                topping1_id = topping1.id_topping();
                if (!discard) {
                    PGComms.modify_topping_quantity_in_edible_inventory(topping1_id, -1);
                }
            }

            if (!line.topping2_name().equals("No topping")) {
                MenuToppingsRowDTO topping2 =
                    find_topping(line.topping2_name());

                if (topping2 == null) {
                    throw new IllegalStateException(
                        "Topping not found: " + line.topping2_name());
                }
                topping2_id = topping2.id_topping();
                if (!discard) {
                    PGComms.modify_topping_quantity_in_edible_inventory(topping2_id, -1);
                }
            }

            // Convert ice level and sugar percent to integer representations for database storage.
            int ice_level;
            int sugar_level;
            boolean hot_chosen = false;
            switch(line.ice_level()){
                case "None": ice_level = 0; break;
                case "Light": ice_level = 1; break;
                case "Regular": ice_level = 2; break;
                case "Hot": ice_level = 0; hot_chosen = true; break;
                default: ice_level = 2; hot_chosen = false; break;
            }
            switch(line.sugar_percent()){
                case 0: sugar_level = 0; break;
                case 25: sugar_level = 1; break;
                case 50: sugar_level = 2; break;
                case 75: sugar_level = 3; break;
                case 100: sugar_level = 4; break;
                default: sugar_level = 4; break;
            }

            if (!discard) {
                // Decrement the inventory quantities for the non edible items used in the order.
                if (!PGComms.modify_topping_quantity_in_nonedible_inventory(1,-1)
                        || !PGComms.modify_topping_quantity_in_nonedible_inventory(2,-1)
                        || !PGComms.modify_topping_quantity_in_nonedible_inventory(3,-1)
                        || !PGComms.modify_topping_quantity_in_nonedible_inventory(4,-1)) {
                    throw new IllegalStateException(
                        "Could not update required non-edible inventory.");
                }
            }

            String status = "completed";

            if (discard) {
                status = "discard";
            }

            PGComms.insert_order_item(
                next_order_id,
                status,
                price_with_tip,
                tip_rate.doubleValue(),
                id_drink,
                topping1_id,
                topping2_id,
                ice_level,
                sugar_level,
                hot_chosen);
        }

        // If all order lines were successfully processed and inserted into the database, return true to indicate a successful checkout.
        return true;
    }
}