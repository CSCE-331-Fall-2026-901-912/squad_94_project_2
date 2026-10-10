package dto;

import java.math.BigDecimal;

// DTO for retrieving information row-by-row from the menu_toppings table in the SQL database.
public record MenuToppingsRowDTO(int id_topping, String name, BigDecimal price) {}