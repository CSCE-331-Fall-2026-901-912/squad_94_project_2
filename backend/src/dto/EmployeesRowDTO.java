package dto;

import java.math.BigDecimal;

public record EmployeesRowDTO(int id_employee, String name, String position, String phone_number, BigDecimal current_pay_rate, int hours_worked_for_week) {}