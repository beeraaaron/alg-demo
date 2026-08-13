package ch.fhnw.algdemo.control.variable;

import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.List;

public class VariableController extends VBox {
    @FXML
    VBox variablesBox;

    public void initializeVariables(List<AlgorithmVariable<?>> variables) {
        for (var variable : variables) {
            var variableDetail = new VariableElement(variable);
            variablesBox.getChildren().add(variableDetail);
        }
    }

    public void clear() {
        variablesBox.getChildren().clear();
    }
}
