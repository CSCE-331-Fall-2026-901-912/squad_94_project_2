package model;

import java.util.ArrayList;
import java.util.List;

import database.PGComms;
import dto.InvEdibleRowDTO;
import dto.OrdersRowDTO;

import java.time.LocalDate;
import java.time.ZoneId;

public class XReportModel {
    

    //Grab the current date in utc then grab the void, completed, and discard totals from the database. Return a string including all the totals by hour.
    public String get_xreport() {
        ZoneId centralTime = ZoneId.of("America/Chicago");
        String curr_date = LocalDate.now(centralTime).minusDays(30).toString(); 
        String report = PGComms.get_xreport_string(curr_date);       
        return report;
    }
}
