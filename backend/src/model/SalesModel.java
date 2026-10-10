package model;

import database.PGComms;

import java.math.BigDecimal;
import java.time.LocalDate;

// Model class for sales analytics-related data, including drinks, toppings, associated prices, total sales, and sales based on specific time periods.
public class SalesModel {
    public BigDecimal get_total_sales() {
        return PGComms.get_sales_total();
    }

    public BigDecimal get_todays_sales() {
        return PGComms.get_sales_total_date(
            LocalDate.now().toString());
    }
}