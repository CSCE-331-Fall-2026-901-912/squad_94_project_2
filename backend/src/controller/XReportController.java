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
    private final Runnable on_added;

    @FXML private TextArea xreport_text_area;


    public XReportController(XReportModel model, Runnable on_added) {
        this.model = model;
        this.on_added = on_added;
    }

    @FXML private void initialize() {
        refresh();
    }
    

    public void refresh() {
        xreport_text_area.setText(model.get_xreport());
        on_added.run();
    }
    
}
