package model;

import javafx.stage.Stage;

public class ProcessingOrders {

    private final Stage stage;

    public ProcessingOrders() { this(null); }
    public ProcessingOrders(Stage stage) { this.stage = stage; }

    public void close() {
        if (stage != null) stage.close();
    }
}