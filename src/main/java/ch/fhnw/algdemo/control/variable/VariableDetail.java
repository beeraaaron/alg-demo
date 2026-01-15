package ch.fhnw.algdemo.control.variable;

import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.SneakyThrows;

public class VariableDetail extends Pane {
    @FXML
    private Label overviewLabel;

    private final AlgorithmVariable<?> variable;

    public VariableDetail(AlgorithmVariable<?> variable) {
        this.variable = variable;
        loadFxController();
    }

    @FXML
    public void initialize() {
        this.overviewLabel.setText(variable.name + " = " + getVariableValue());
    }

    private String getVariableValue() {
        if (variable.value != null) {
            return variable.value.toString();
        }
        return "null";
    }

    private void handleX() {
        if (variable.value instanceof Integer) {
            Integer intValue = (Integer) variable.value;
        } else if (variable.value instanceof String) {
            String strValue = (String) variable.value;
        }
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("variable-detail.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
