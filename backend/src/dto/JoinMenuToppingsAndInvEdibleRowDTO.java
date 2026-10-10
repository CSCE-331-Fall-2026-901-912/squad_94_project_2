package dto;

// DTO for retrieving information row-by-row from the join_menu_toppings_and_inv_edible table in the SQL database.
public record JoinMenuToppingsAndInvEdibleRowDTO(int id_join_menu_toppings_and_inv_edible, int id_topping, int id_edible) {}