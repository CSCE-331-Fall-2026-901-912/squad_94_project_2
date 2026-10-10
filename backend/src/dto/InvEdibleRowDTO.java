package dto;

// DTO for retrieving information row-by-row from the inv_edible table in the SQL database.
public record InvEdibleRowDTO(int id_edible, String name, int amount_servings) {}