package dto;

/**
 * Data transfer object to store all data from a join_menu_toppings_and_inv_edible SQL table row,
 * or create a structure to properly insert a new row into this SQL table.
 * Data types of members directly match to types in a row from the table.
 * No implicit lossy conversion occur.
 * This join table associates toppings in the menu_toppings table with edible items in the inv_edibles table.
 * This association details the edible items--the ingredients--that are fundamentally needed to one of a specific topping.
 *
 * @author Squad 94 - Rigo Chiti
 * @param id_join_menu_toppings_and_inv_edible A unique ID (PK) identifying one association of an id_topping to an id_edible.
 * @param id_topping A unique ID identifying one topping in menu_toppings.
 * @param id_edible A unique ID identifying one edible item in inv_edibles.
 */
// DTO for retrieving information row-by-row from the join_menu_toppings_and_inv_edible table in the SQL database.
public record JoinMenuToppingsAndInvEdibleRowDTO(int id_join_menu_toppings_and_inv_edible, int id_topping, int id_edible) {}