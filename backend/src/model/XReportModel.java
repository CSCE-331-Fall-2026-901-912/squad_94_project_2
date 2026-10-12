package model;

import java.util.ArrayList;
import java.util.List;

import database.PGComms;
import dto.InvEdibleRowDTO;
import dto.OrdersRowDTO;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class XReportModel {
    


    public String get_xreport() {
        //ArrayList<OrdersRowDTO> issue_query_type_rows_Orders(String query)

        //List<OrdersRowDTO> last_order = PGComms.issue_query_type_rows_Orders("SELECT * FROM orders ORDER BY time_completed_at DESC LIMIT 1");
        String curr_date = ""+LocalDate.now(ZoneOffset.UTC);//.minusDays(1);
        System.out.println("curr date: " +curr_date);
        String report = PGComms.get_xreport_string(curr_date); 
        System.out.println("xreport: " +report);       
        return report;
    }
}
