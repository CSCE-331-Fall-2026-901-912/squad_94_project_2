package model;

import database.PGComms;
import dto.EmployeesRowDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Model class for managing employee-related data, including retrieving, adding, updating, and deleting employee records.
 * 
 * @author Squad 94 - Sanjana Ram
 */
public class EmployeeModel {

    /** 
     * Retrieves a list of all employees from the database.
     * @return a list of EmployeesRowDTO objects representing the employees
    */
    public List<EmployeesRowDTO> get_all() {
        return PGComms.issue_query_type_rows_Employees("SELECT * FROM employees ORDER BY id_employee;");
    }

    /**
     * Adds a new employee to the database with the provided details.
     * @param name the name of the employee
     * @param position the position of the employee (eg. Barista, Manager)
     * @param phone the phone number of the employee
     * @param pay the current pay rate of the employee
     * @param hours the number of hours worked for the week by the employee
     * @return true if the employee was successfully added, false otherwise
     */
    public boolean add(
            String name,
            String position,
            String phone,
            BigDecimal pay,
            int hours) {
        return PGComms.add_employee(name, position, phone, pay, hours);
    }

    /**
     * Updates an existing employee's details in the database.
     * @param id the ID of the employee to update
     * @param name the new name of the employee
     * @param position the new position of the employee
     * @param phone the new phone number of the employee
     * @param pay the new current pay rate of the employee
     * @param hours the new number of hours worked for the week by the employee
     * @return true if the employee was successfully updated, false otherwise
     */
    public boolean update(
            int id,
            String name,
            String position,
            String phone,
            BigDecimal pay,
            int hours) {
        return PGComms.update_employee(id, name, position, phone, pay, hours);
    }

    /**
     * Deletes an employee from the database based on the provided ID.
     * @param id the ID of the employee to delete
     * @return true if the employee was successfully deleted, false otherwise
     */
    public boolean delete(int id) {
        return PGComms.delete_employee(id);
    }
}