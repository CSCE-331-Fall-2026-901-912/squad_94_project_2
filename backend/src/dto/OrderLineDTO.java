package dto;

import java.math.BigDecimal;

// DTO for the simplified order details given line-by-line (one order per line) on the Order Menu.
public record OrderLineDTO(String drink_name, String topping1_name, String topping2_name, String ice_level, int sugar_percent, BigDecimal price) {}