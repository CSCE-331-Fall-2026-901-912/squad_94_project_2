package model;

import database.PGComms;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Model class for sales analytics-related data, including drinks, toppings, associated prices, total sales, and sales based on specific time periods.
 * 
 * @author Squad 94 - Natalie Gonzalez
 */
public class SalesModel {
    /**
     * Retrieves the total sales amount from the database.
     * @return the total sales as a BigDecimal
     */
    public BigDecimal get_total_sales() {
        return PGComms.get_sales_total();
    }

    /**
     * Retrieves the total sales amount for the current day from the database.
     * @return the total sales for today as a BigDecimal
     */
    public BigDecimal get_todays_sales() {
        return PGComms.get_sales_total_date(
            LocalDate.now().toString());
    }
}