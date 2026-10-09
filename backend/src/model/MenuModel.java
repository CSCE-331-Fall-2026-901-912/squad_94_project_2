package model;

import database.PGComms;
import dto.InvEdibleRowDTO;
import dto.JoinMenuDrinksAndInvEdibleRowDTO;
import dto.MenuDrinksRowDTO;
import dto.MenuToppingsRowDTO;

import java.math.BigDecimal;
import java.util.List;

public class MenuModel {
    public List<MenuDrinksRowDTO> get_drinks() {
        return PGComms.issue_query_type_rows_MenuDrinks("SELECT * FROM menu_drinks ORDER BY id_drink");
    }

    public List<MenuToppingsRowDTO> get_toppings() {
        return PGComms.issue_query_type_rows_MenuToppings("SELECT * FROM menu_toppings ORDER BY id_topping");
    }

    public List<InvEdibleRowDTO> get_ingredients() {
        return PGComms.issue_query_type_rows_InvEdible("SELECT * FROM inv_edible");
    }

    public List<JoinMenuDrinksAndInvEdibleRowDTO>
    get_drink_ingredients(int drink_id) {
        return PGComms.issue_query_type_rows_JoinMenuDrinksAndInvEdible(
            "SELECT id_join_menu_drinks_and_inv_edible, " +
            "id_drink, id_edible " +
            "FROM join_menu_drinks_and_inv_edible " +
            "WHERE id_drink = " + drink_id);
    }

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

    public boolean delete_drink(int drink_id) {
        return PGComms.delete_drink(drink_id);
    }

    public boolean add_topping(String name, BigDecimal price) {
        return PGComms.add_topping_with_inventory(name, price);
    }

    public boolean update_topping(
            int topping_id,
            String name,
            BigDecimal price) {
        return PGComms.update_topping_with_inventory(
                topping_id,
                name,
                price);
    }

    public boolean delete_topping(int topping_id) {
        return PGComms.delete_topping(topping_id);
    }

    //used for the cashier side
    public List<String> get_drink_names_by_type(String type) {
        return PGComms.get_drink_names_by_type(type);
    }

    public List<String> get_topping_names() {
        return PGComms.get_topping_names();
    }

    public boolean is_hot_available(String drink_name) {
        return PGComms.is_hot_available(drink_name);
    }

    public BigDecimal get_drink_price(String drink_name) {
        return PGComms.get_drink_price(drink_name);
    }

    public BigDecimal get_topping_price(String topping_name) {
        return PGComms.get_topping_price(topping_name);
    }
}