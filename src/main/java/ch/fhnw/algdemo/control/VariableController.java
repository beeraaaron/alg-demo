package ch.fhnw.algdemo.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class VariableController extends VBox {
    List<Object> variables;

    @FXML
    private VBox variablesBox;

    public VariableController(List<Object> variables) {
        this.variables = variables;
        var loader = new FXMLLoader(getClass().getResource("variables.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        for (var variable : variables) {
            var variableDetail = new VariableDetail(variable);
            variablesBox.getChildren().addAll(variableDetail);
        }
    }
}
