package dto;

import java.math.BigDecimal;

/**
 * Data transfer object to store all data from a menu_toppings SQL table row,
 * or create a structure to properly insert a new row into the menu_toppings SQL table.
 * Data types of members directly match to types in a menu_toppings row.
 * No implicit lossy conversion occur.
 *
 * @author Squad 94 - Rigo Chiti
 * @param id_topping A unique ID (PK) identifying a topping in the menu_toppings table.
 * @param name The name of the topping.
 * @param price The base price of the topping, as how much extra charge is incurred when one of it is added to a drink order.
 */
// DTO for retrieving information row-by-row from the menu_toppings table in the SQL database.
public record MenuToppingsRowDTO(int id_topping, String name, BigDecimal price) {}