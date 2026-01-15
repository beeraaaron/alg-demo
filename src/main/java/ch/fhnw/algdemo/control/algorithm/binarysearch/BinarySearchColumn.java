package ch.fhnw.algdemo.control.algorithm.binarysearch;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;

public class BinarySearchColumn extends VBox {
    @FXML
    private TextField valueField;
    @FXML
    private Label indexLabel;
    @FXML
    private Label variableLabel;

    private Integer value;
    private Integer index;
    private String variable;

    public BinarySearchColumn(Integer value, Integer index, String variable) {
        this.value = value;
        this.index = index;
        this.variable = variable;
        loadFxController();
    }

    @FXML
    public void initialize() {
        this.valueField.setText(value.toString());
        this.indexLabel.setText(index.toString());
        this.variableLabel.setText(variable);
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("binary-search-column.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
