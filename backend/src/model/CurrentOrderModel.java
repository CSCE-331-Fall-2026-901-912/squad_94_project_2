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

public class CurrentOrderModel {
    // List to hold the current order lines, each representing an item in the order.
    private final List<OrderLineDTO> lines = new ArrayList<>();
    // StringProperty to hold the current order details for display purposes.
    private final StringProperty current_order = new SimpleStringProperty("Item1: ");
    // StringProperty to hold the total price of the current order for display purposes.
    private final StringProperty order_total = new SimpleStringProperty("Total: 0.00");

    // Getters for the current order and total price properties, allowing other parts of the application to bind to these properties for UI updates.
    public StringProperty get_current_order() {
        return current_order;
    }

    public StringProperty get_order_total() {
        return order_total;
    }

    public List<OrderLineDTO> get_lines() {
        return List.copyOf(lines);
    }

    public BigDecimal get_total() {
        return lines.stream()
            .map(OrderLineDTO::price)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Add a new item to the current order and refresh the display to reflect the updated order details and total price.
    public void add_item(OrderLineDTO line) {
        lines.add(line);
        refresh_display();
    }

    public void clear() {
        lines.clear();
        refresh_display();
    }

    // Refresh the display of the current order and total price based on the items in the order.
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
    private int get_last_orderID(){
        ArrayList<OrdersRowDTO> order_rows;
        order_rows = PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order DESC LIMIT 1;");
        
        int last_num = order_rows.getFirst().id_order();
        return last_num;
    }
    
    // Find a drink by its name in the menu database and return its corresponding MenuDrinksRowDTO.
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
    public boolean checkout(BigDecimal tip_rate) {

        // Ensure that the order is not empty before proceeding with checkout.
        if(lines.isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty order.");
        }

        for(OrderLineDTO line : lines) {
            // Calculate tips individually to make it easier to insert each drink as its own line
            double price_with_tip = line.price().multiply(BigDecimal.ONE.add(tip_rate)).setScale(2, RoundingMode.HALF_UP).doubleValue();
            int next_order_id = get_last_orderID() + 1;
            int id_drink = find_drink(line.drink_name()).id_drink();
            
            int topping1_id = 0;
            int topping2_id = 0;

            decrement_drink_ingredients(id_drink);

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
                PGComms.modify_topping_quantity_in_edible_inventory(topping1_id, -1);
            }

            if (!line.topping2_name().equals("No topping")) {
                MenuToppingsRowDTO topping2 =
                    find_topping(line.topping2_name());

                if (topping2 == null) {
                    throw new IllegalStateException(
                        "Topping not found: " + line.topping2_name());
                }
                topping2_id = topping2.id_topping();
                PGComms.modify_topping_quantity_in_edible_inventory(topping2_id, -1);
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

            // Decrement the inventory quantities for the non edible items used in the order.
            PGComms.modify_topping_quantity_in_nonedible_inventory(1,-1);
            PGComms.modify_topping_quantity_in_nonedible_inventory(2,-1);
            PGComms.modify_topping_quantity_in_nonedible_inventory(3,-1);
            PGComms.modify_topping_quantity_in_nonedible_inventory(4,-1);

            PGComms.insert_order_item(
                next_order_id,
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