package ch.fhnw.algdemo.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BinarySearchController extends Pane implements AlgorithmController {
    List<Object> variables =  List.of(4, 6, 10);

    @FXML
    private Label label;

    public BinarySearchController() {
        var loader = new FXMLLoader(getClass().getResource("binary-search.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return "Binary Search";
    }

    @Override
    public List<Object> getVariables() {
        return variables;
    }

    @FXML
    public void initialize() {
        this.label.setText(getName());
    }
}
