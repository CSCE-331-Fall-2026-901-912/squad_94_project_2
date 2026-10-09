package dto;

import java.math.BigDecimal;

public record MenuDrinksRowDTO(int id_drink, String name, BigDecimal price, String type, boolean hot_available, boolean is_non_caffeinated) {}