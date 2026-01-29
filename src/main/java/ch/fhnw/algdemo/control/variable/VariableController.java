package ch.fhnw.algdemo.control.variable;

import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.List;

public class VariableController extends VBox {
    @FXML
    private VBox variablesBox;

    List<AlgorithmVariable<?>> variables;

    public void initializeVariables(List<AlgorithmVariable<?>> variables) {
        this.variables = variables;
        for (var variable : this.variables) {
            var variableDetail = new VariableElement(variable);
            variablesBox.getChildren().add(variableDetail);
        }
    }

    public void clear() {
        variablesBox.getChildren().clear();
    }
}
