package model;

import database.PGComms;
import dto.EmployeesRowDTO;

import java.math.BigDecimal;
import java.util.List;

public class EmployeeModel {
    public List<EmployeesRowDTO> get_all() {
        return PGComms.issue_query_type_rows_Employees("SELECT * FROM employees ORDER BY id_employee;");
    }

    public boolean add(
            String name,
            String position,
            String phone,
            BigDecimal pay,
            int hours) {
        return PGComms.add_employee(name, position, phone, pay, hours);
    }

    public boolean update(
            int id,
            String name,
            String position,
            String phone,
            BigDecimal pay,
            int hours) {
        return PGComms.update_employee(id, name, position, phone, pay, hours);
    }

    public boolean delete(int id) {
        return PGComms.delete_employee(id);
    }
}