package controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import model.InventoryModel;
import model.XReportModel;

public class XReportController {

    private final XReportModel model;

    @FXML private TextArea xreport_text_area;


    public XReportController(XReportModel model) {
        this.model = model;
    }


    
   
    
}
