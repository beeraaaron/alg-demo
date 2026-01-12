package ch.fhnw.algdemo.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class VariableDetail extends Pane {
    private Object variable;
    @FXML
    private Label overviewLabel;

    public VariableDetail(Object variable) {
        this.variable = variable;

        var loader = new FXMLLoader(getClass().getResource("variable-detail.fxml"));
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
        this.overviewLabel.setText(variable.getClass().getCanonicalName() + " = " + variable.toString());
    }
}
