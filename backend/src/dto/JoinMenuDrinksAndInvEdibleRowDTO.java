package dto;

// DTO for retrieving information row-by-row from the join_menu_drinks_and_inv_edible table in the SQL database.
public record JoinMenuDrinksAndInvEdibleRowDTO(int id_join_menu_drinks_and_inv_edible, int id_drink, int id_edible) {}