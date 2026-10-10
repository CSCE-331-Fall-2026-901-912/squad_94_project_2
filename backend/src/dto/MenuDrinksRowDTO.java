package dto;

import java.math.BigDecimal;

// DTO for retrieving information row-by-row from the menu_drinks table in the SQL database.
public record MenuDrinksRowDTO(int id_drink, String name, BigDecimal price, String type, boolean hot_available, boolean is_non_caffeinated) {}