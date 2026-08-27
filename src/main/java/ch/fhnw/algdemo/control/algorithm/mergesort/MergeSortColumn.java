package ch.fhnw.algdemo.control.algorithm.mergesort;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.SneakyThrows;

public class MergeSortColumn extends GridPane {
    private final SimpleIntegerProperty value;

    @FXML
    TextField valueField;

    public MergeSortColumn(SimpleIntegerProperty value) {
        this.value = value;
        loadFxController();
    }

    @FXML
    public void initialize() {
        this.valueField.textProperty().bind(this.value.asString());
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("merge-sort-column.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
