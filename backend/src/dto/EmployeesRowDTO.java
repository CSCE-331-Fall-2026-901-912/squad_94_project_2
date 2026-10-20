package dto;

import java.math.BigDecimal;

/**
 * Data transfer object to store all data from an employee SQL table row,
 * or create a structure to properly insert a new row into the employee SQL table.
 * Data types of members directly match to types in an employee row.
 * No implicit lossy conversion occur.
 *
 * @author Squad 94 - Rigo Chiti
 * @param id_employee A unique ID (PK) identifying an employee in the employees table.
 * @param name The name of the employee.
 * @param position The named position the employee holds.
 * @param phone_number The employee's phone number.
 * @param current_pay_rate The current hourly pay rate the employee has.
 * @param hours_worked_for_week The number of hours the employee is scheduled to work for the week.
 */
// DTO for retrieving information row-by-row from the employees table in the SQL database.
public record EmployeesRowDTO(int id_employee, String name, String position, String phone_number, BigDecimal current_pay_rate, int hours_worked_for_week) {}