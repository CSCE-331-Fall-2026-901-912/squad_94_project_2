package dto;

/**
 * Data transfer object to store all data from a join_menu_drinks_and_inv_edible SQL table row,
 * or create a structure to properly insert a new row into this SQL table.
 * Data types of members directly match to types in a row from the table.
 * No implicit lossy conversion occur.
 * This join table associates drinks in the menu_drinks table with edible items in the inv_edibles table.
 * This association details the edible items--the ingredients--that are fundamentally needed to create one of a specific drink.
 *
 * @author Squad 94 - Rigo Chiti
 * @param id_join_menu_drinks_and_inv_edible A unique ID (PK) identifying one association of an id_drink to an id_edible.
 * @param id_drink A unique ID identifying one drink in menu_drinks.
 * @param id_edible A unique ID identifying one edible item in inv_edibles.
 */
// DTO for retrieving information row-by-row from the join_menu_drinks_and_inv_edible table in the SQL database.
public record JoinMenuDrinksAndInvEdibleRowDTO(int id_join_menu_drinks_and_inv_edible, int id_drink, int id_edible) {}