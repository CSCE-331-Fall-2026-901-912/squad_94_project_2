package dto;

/**
 * Data transfer object to store all data from an inv_nonedible SQL table row,
 * or create a structure to properly insert a new row into the inv_nonedible SQL table.
 * Data types of members directly match to types in an inv_nonedible row.
 * No implicit lossy conversion occur.
 *
 * @author Squad 94 - Rigo Chiti
 * @param id_nonedible A unique ID (PK) identifying a non-edible item in the inv_nonedible table.
 * @param name The name of the non-edible item.
 * @param amount The amount of the non-edible item in the inventory. Non-edible items are discrete and unambiguous in quantification (ex: one straw is one straw).
 */
// DTO for retrieving information row-by-row from the inv_nonedible table in the SQL database.
public record InvNonEdibleRowDTO(int id_nonedible, String name, int amount) {}