package dto;

import java.time.Instant;
import java.math.BigDecimal;

public record OrdersRowDTO(int id_order, boolean completed, Instant time_created_at, Instant time_completed_at, BigDecimal total_spent, int id_employee, BigDecimal tip, int id_drink, int id_topping1, int id_topping2, int ice_level, int sugar_level, boolean hot_chosen) {}