package model;

import java.util.ArrayList;
import java.util.List;

import database.PGComms;
import dto.InvEdibleRowDTO;
import dto.OrdersRowDTO;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Model class for retrieving the xreport data.
 * 
 * @author Squad 94 - Mairead Finnerty
 */
public class XReportModel {
    

    /**
     * Grab the current date in utc and convert to central time, then grab the void, completed, and discard totals from the database. Return a string including all the totals by hour.
     * @return a string with the completed, void, and discard orders per hour for the current day
     */
    public String get_xreport() {
        ZoneId centralTime = ZoneId.of("America/Chicago");
        String curr_date = LocalDate.now(centralTime).toString();
        String report = PGComms.get_xreport_string(curr_date);       
        return report;
    }
}
