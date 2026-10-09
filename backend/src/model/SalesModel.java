package model;

import database.PGComms;
import dto.IDNameValDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class SalesModel {
    public BigDecimal get_total_sales() {
        return PGComms.get_sales_total();
    }

    public BigDecimal get_todays_sales() {
        return PGComms.get_sales_total_date(
            LocalDate.now().toString());
    }
}