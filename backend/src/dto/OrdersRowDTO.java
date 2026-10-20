package dto;

import java.time.OffsetDateTime;
import java.math.BigDecimal;

/**
 * Data transfer object to store all data from an orders SQL table row,
 * or create a structure to properly insert a new row into the orders SQL table.
 * Data types of members directly match to types in an orders row.
 * No implicit lossy conversion occur.
 *
 * @author Squad 94 - Rigo Chiti
 * @param id_order A unique ID (PK) identifying an order in the orders table.
 * @param status The status of the order, which is one of the following: "completed", "void", or "discard".
 * @param time_created_at UTC timestamp of when the order was formerly created (added to the database). Therefore, this time can also be when the order was completed, voided, or discarded.
 * @param total_spent The total amount of money spent on the order. Numeric, monetary representation.
 * @param id_employee A unique ID identifying the employee who completed this order (matches to PK id_employee in the employees table).
 * @param tip The percentage tip made on the order by the customer.
 * @param id_drink A unique ID identifying the drink for this order (matches to PK id_drink in the menu_drinks table).
 * @param id_topping1 A unique ID identifying the first topping chosen for the order (matches to PK id_topping in the menu_toppings table). If null, this means no toppings were chosen.
 * @param id_topping2 A unique ID identifying the second topping chosen for the order (matches to PK id_topping in the menu_toppings table). If null, this means only one topping was chosen (see value in id_topping1).
 * @param ice_level The ice level chosen, which is one of the following: 0 meaning no ice or a hot drink. 1 meaning "light" ice. 2 meaning "regular" ice.
 * @param sugar_level The sugar level chosen, which is one of the following: 0 meaning 0% sugar. 1 meaning 25%. 2 meaning 50%. 3 meaning 75%. 4 meaning 100%.
 * @param hot_chosen Always false if the drink cannot be served hot. If the drink can be served hot, indicates if the customer chose for a hot drink or not.
 */
// DTO for retrieving information row-by-row from the orders table in the SQL database.
public record OrdersRowDTO(int id_order, String status, OffsetDateTime time_created_at, BigDecimal total_spent, int id_employee, BigDecimal tip, int id_drink, int id_topping1, int id_topping2, int ice_level, int sugar_level, boolean hot_chosen) {}
