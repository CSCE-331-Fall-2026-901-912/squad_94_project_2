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

/**
 * Controller for displaying the X-report of the current day by hour
 *
 * @author Squad 94 - Mairead Finnerty
 */
public class XReportController {

    private final XReportModel model;
    private final Runnable on_added;

    @FXML private TextArea xreport_text_area;

    /**
     * Creates controller constructor for syncing the model with this controller.
     *
     * @param model the xreport model used to display the xreport data
     * @param on_added method that allows page to be automatically refreshed so changes show up.
     */
    public XReportController(XReportModel model, Runnable on_added) {
        this.model = model;
        this.on_added = on_added;
    }

    /**
     * Refreshes the page to ensure updates are shown to the user.
     */
    @FXML private void initialize() {
        refresh();
    }
    
    /**
     * Ensures the xreport displays are up to date with the latest changes to the database.
     */
    public void refresh() {
        xreport_text_area.setText(model.get_xreport());
        on_added.run();
    }
    
}
