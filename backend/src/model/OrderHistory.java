package model;

import database.PGComms;
import dto.OrdersRowDTO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.stage.Stage;

public class OrderHistory {
    private final Stage stage;

    public OrderHistory() { this(null); }
    public OrderHistory(Stage stage) { this.stage = stage; }

    public void close() {
        if (stage != null) stage.close();
    }
}