package dto;

import java.math.BigDecimal;

public record OrderLineDTO(String drink_name, String topping1_name, String topping2_name, String ice_level, int sugar_percent, BigDecimal price) {}