package ch.fhnw.algdemo.control.variable;

import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import lombok.SneakyThrows;

public class VariableElement extends Label {
    private final AlgorithmVariable<?> variable;

    public VariableElement(AlgorithmVariable<?> variable) {
        this.variable = variable;
        loadFxController();
    }

    @FXML
    public void initialize() {
        this.setText(variable.getName() + " = " + getVariableValue());
    }

    private String getVariableValue() {
        if (variable.getValue() != null) {
            return variable.getValue().toString();
        }
        return "null";
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("variable-element.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
