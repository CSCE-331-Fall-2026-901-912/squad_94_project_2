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
    private final List<OrderLineDTO> lines = new ArrayList<>();
    private final StringProperty current_order = new SimpleStringProperty("Item1: ");
    private final StringProperty order_total = new SimpleStringProperty("Total: 0.00");

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

    public void add_item(OrderLineDTO line) {
        lines.add(line);
        refresh_display();
    }

    public void clear() {
        lines.clear();
        refresh_display();
    }

    public void refresh_display() {
        if (lines.isEmpty()) {
            current_order.set("Item1: ");
            order_total.set("Total: 0.00");
            return;
        }

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
    private int get_last_orderID(){
        ArrayList<OrdersRowDTO> order_rows;
        order_rows = PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY id_order DESC LIMIT 1;");
        
        int last_num = order_rows.getFirst().id_order();
        return last_num;
    }
    
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


    public boolean checkout(BigDecimal tip_rate) {
        if(lines.isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty order.");
        }

        for(OrderLineDTO line : lines) {
            double price_with_tip = line.price().multiply(BigDecimal.ONE.add(tip_rate)).setScale(2, RoundingMode.HALF_UP).doubleValue();
            int next_order_id = get_last_orderID() + 1;
            int id_drink = find_drink(line.drink_name()).id_drink();
            
            int topping1_id = 0;
            int topping2_id = 0;

            decrement_drink_ingredients(id_drink);

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
        return true;
    }
}