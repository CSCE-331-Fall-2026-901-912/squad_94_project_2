package dto;

/**
 * Data transfer object to store all data from an inv_edible SQL table row,
 * or create a structure to properly insert a new row into the inv_edible SQL table.
 * Data types of members directly match to types in an inv_edible row.
 * No implicit lossy conversion occur.
 *
 * @author Squad 94 - Rigo Chiti
 * @param id_edible A unique ID (PK) identifying an edible item in the inv_edible table.
 * @param name The name of the edible item.
 * @param amount_servings The amount of the edible item in the inventory, where a "serving" means enough of the item such that it can be used as an ingredient or topping for a drink.
 */
// DTO for retrieving information row-by-row from the inv_edible table in the SQL database.
public record InvEdibleRowDTO(int id_edible, String name, int amount_servings) {}