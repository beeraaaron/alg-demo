package ch.fhnw.algdemo.control.algorithm.binarysearch;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchColumn extends VBox {
    @FXML
    TextField valueField;
    @FXML
    Label indexLabel;
    @FXML
    Label variableLabelOne;
    @FXML
    Label variableLabelTwo;
    @FXML
    Label variableLabelThree;

    private final Integer value;
    @Getter
    private final Integer index;
    private List<String> variables;

    public BinarySearchColumn(Integer value, Integer index) {
        this.value = value;
        this.index = index;
        this.variables = new ArrayList<>();
        loadFxController();
        disableValueField();
    }

    @FXML
    public void initialize() {
        this.valueField.setText(value.toString());
        this.indexLabel.setText(index.toString());
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
        updateVariableLabels();
    }

    public void setMinColor() {
        valueField.getStyleClass().add("min-text-field");
    }

    public void setMaxColor() {
        valueField.getStyleClass().add("max-text-field");
    }

    public void removeColors() {
        valueField.getStyleClass().remove("max-text-field");
        valueField.getStyleClass().remove("min-text-field");

    }

    public void disableValueField() {
        valueField.setDisable(true);
        valueField.setFocusTraversable(false);
        valueField.getStyleClass().add("disabled-value-field");
    }

    public void enableValueField() {
        valueField.setDisable(false);
        valueField.setFocusTraversable(true);
        valueField.getStyleClass().remove("disabled-value-field");
        valueField.getStyleClass().add("default-text-field");
    }

    private void updateVariableLabels() {
        this.variableLabelOne.setText(variables.size() > 0 ? variables.get(0) : "");
        this.variableLabelTwo.setText(variables.size() > 1 ? variables.get(1) : "");
        this.variableLabelThree.setText(variables.size() > 2 ? variables.get(2) : "");
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("binary-search-column.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
