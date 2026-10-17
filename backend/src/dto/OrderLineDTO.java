package dto;

import java.math.BigDecimal;

/**
 * Data transfer object to store some data related to an order.
 * NOTE THE FOLLOWING:
 * This DTO does not directly correspond or conform to the structure of a row in the orders SQL table.
 * This DTO does not contain all data necessary to insert a new row into the orders table.
 * Primarily a means to store and retrieve details about an order before it is finalized
 * (before a row is formally added to the orders SQL table to signify the order has been made and officially logged).
 *
 * @author Squad 94 - Sanjana Ram
 * @param drink_name The name of the drink which was chosen for this order.
 * @param topping1_name The name of the first topping which was chosen for this order. If null, this means no toppings were chosen.
 * @param topping2_name The name of the second topping which was chosen for this order. If null, this means only one topping was chosen (see name in topping1_name).
 * @param ice_level The ice level chosen for this order, as in "None", "Light", "Regular", or "Hot" (hot also means no ice, but drink is chosen to be hot if applicable).
 * @param sugar_percent The percentage of sugar chosen for the order. Specifically, the whole number in percentage notation.
 * @param price The total cost of the order, before a tip will be applied. Numeric, monetary representation.
 */
// DTO for the simplified order details given line-by-line (one order per line) on the Order Menu.
public record OrderLineDTO(String drink_name, String topping1_name, String topping2_name, String ice_level, int sugar_percent, BigDecimal price) {}