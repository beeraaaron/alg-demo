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
    private TextField valueField;
    @FXML
    private Label indexLabel;
    @FXML
    private Label variableLabelOne;
    @FXML
    private Label variableLabelTwo;
    @FXML
    private Label variableLabelThree;

    private Integer value;
    @Getter
    private Integer index;
    private List<String> variables;

    public BinarySearchColumn(Integer value, Integer index) {
        this.value = value;
        this.index = index;
        this.variables = new ArrayList<>();
        loadFxController();
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
        valueField.getStyleClass().add("default-text-field");
    }

    private void updateVariableLabels() {
        if (variables.isEmpty()) {
            this.variableLabelOne.setText("");
            this.variableLabelTwo.setText("");
            this.variableLabelThree.setText("");
        } else if (variables.size() == 1) {
            this.variableLabelOne.setText(variables.getFirst());
            this.variableLabelTwo.setText("");
            this.variableLabelThree.setText("");
        } else if (variables.size() == 2) {
            this.variableLabelOne.setText(variables.getFirst());
            this.variableLabelTwo.setText(variables.get(1));
            this.variableLabelThree.setText("");
        } else if (variables.size() == 3) {
            this.variableLabelOne.setText(variables.getFirst());
            this.variableLabelTwo.setText(variables.get(1));
            this.variableLabelThree.setText(variables.get(2));
        }
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("binary-search-column.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
