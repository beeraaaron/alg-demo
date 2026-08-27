package ch.fhnw.algdemo.control.algorithm.mergesort;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.SneakyThrows;

public class MergeSortColumn extends GridPane {
    private final SimpleIntegerProperty number;
    private final SimpleBooleanProperty visibility;
    private final SimpleBooleanProperty highlighted;

    @FXML
    TextField valueField;

    public MergeSortColumn(SimpleIntegerProperty number, SimpleBooleanProperty visibility, SimpleBooleanProperty highlighted) {
        this.number = number;
        this.visibility = visibility;
        this.highlighted = highlighted;
        loadFxController();
    }

    @FXML
    public void initialize() {
        valueField.textProperty().bind(this.number.asString());
        valueField.visibleProperty().bind(this.visibility);
        valueField.managedProperty().bind(this.visibility);
        highlighted.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                valueField.getStyleClass().add("cell-highlight");
            } else {
                valueField.getStyleClass().remove("cell-highlight");
            }
        });
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("merge-sort-column.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
