package model;

import javafx.stage.Stage;

public class SalesAnalytics {

    private final Stage stage;

    public SalesAnalytics() { this(null); }
    public SalesAnalytics(Stage stage) { this.stage = stage; }

    public void close() {
        if (stage != null) stage.close();
    }
}
