package ch.fhnw.algdemo.control.algorithm.mergesort;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.SneakyThrows;

public class MergeSortColumn extends GridPane {
    private final Integer value;
    private final int index;

    @FXML
    TextField valueField;

    public MergeSortColumn(Integer value, int index) {
        this.value = value;
        this.index = index;
        loadFxController();
    }

    @FXML
    public void initialize() {
        this.valueField.setText(value.toString());
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("merge-sort-column.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
