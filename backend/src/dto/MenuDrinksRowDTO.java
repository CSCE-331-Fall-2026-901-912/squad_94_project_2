package dto;

import java.math.BigDecimal;

/**
 * Data transfer object to store all data from a menu_drinks SQL table row,
 * or create a structure to properly insert a new row into the menu_drinks SQL table.
 * Data types of members directly match to types in a menu_drinks row.
 * No implicit lossy conversion occur.
 *
 * @author Squad 94 - Rigo Chiti
 * @param id_drink A unique ID (PK) identifying a drink in the menu_drinks table.
 * @param name The name of the drink.
 * @param price The base price of the drink, as in how much it costs in an order where no toppings (extra charge) are added.
 * @param type The category the drink is placed under on the menu.
 * @param hot_available If it is possible to have the drink served hot or not.
 * @param is_non_caffeinated If the drink is non-caffeinated or is caffeinated.
 */
// DTO for retrieving information row-by-row from the menu_drinks table in the SQL database.
public record MenuDrinksRowDTO(int id_drink, String name, BigDecimal price, String type, boolean hot_available, boolean is_non_caffeinated) {}