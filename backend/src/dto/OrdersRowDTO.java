package dto;

import java.time.OffsetDateTime;
import java.math.BigDecimal;

// DTO for retrieving information row-by-row from the orders table in the SQL database.
public record OrdersRowDTO(int id_order, String status, OffsetDateTime time_created_at, BigDecimal total_spent, int id_employee, BigDecimal tip, int id_drink, int id_topping1, int id_topping2, int ice_level, int sugar_level, boolean hot_chosen) {}
