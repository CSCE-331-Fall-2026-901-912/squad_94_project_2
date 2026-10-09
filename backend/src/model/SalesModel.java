package model;

import database.PGComms;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SalesModel {
    public BigDecimal get_total_sales() {
        return PGComms.get_sales_total();
    }

    public BigDecimal get_todays_sales() {
        return PGComms.get_sales_total_date(
            LocalDate.now().toString());
    }
}