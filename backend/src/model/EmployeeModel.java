package model;

import database.PGComms;
import dto.EmployeesRowDTO;

import java.math.BigDecimal;
import java.util.List;

public class EmployeeModel {

    // Retrieve all employees from the database (used to populat the table in EmployeeController).
    public List<EmployeesRowDTO> get_all() {
        return PGComms.issue_query_type_rows_Employees("SELECT * FROM employees ORDER BY id_employee;");
    }

    // Add a new employee to the database with the provided details.
    public boolean add(
            String name,
            String position,
            String phone,
            BigDecimal pay,
            int hours) {
        return PGComms.add_employee(name, position, phone, pay, hours);
    }
    // Update an existing employee's details in the database based on their ID.
    public boolean update(
            int id,
            String name,
            String position,
            String phone,
            BigDecimal pay,
            int hours) {
        return PGComms.update_employee(id, name, position, phone, pay, hours);
    }
    // Delete an employee from the database based on their ID.
    public boolean delete(int id) {
        return PGComms.delete_employee(id);
    }
}