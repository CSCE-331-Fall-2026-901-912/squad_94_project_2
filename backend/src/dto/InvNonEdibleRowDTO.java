package dto;

// DTO for retrieving information row-by-row from the inv_nonedible table in the SQL database.
public record InvNonEdibleRowDTO(int id_nonedible, String name, int amount) {}