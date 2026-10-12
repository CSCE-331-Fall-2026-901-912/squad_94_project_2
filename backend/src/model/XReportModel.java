package model;

import java.util.ArrayList;
import java.util.List;

import database.PGComms;
import dto.InvEdibleRowDTO;
import dto.OrdersRowDTO;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class XReportModel {
    

    //Grab the current date in utc then grab the void, completed, and discard totals from the database. Return a string including all the totals by hour.
    public String get_xreport() {
    
        String curr_date = ""+LocalDate.now(ZoneOffset.UTC);
        System.out.println("curr date: " +curr_date);
        String report = PGComms.get_xreport_string(curr_date); 
        System.out.println("xreport: " +report);       
        return report;
    }
}
