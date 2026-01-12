package ch.fhnw.algdemo.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class MergeSortController extends Pane implements AlgorithmController {
    @FXML
    private Label label;

    public MergeSortController() {
        var loader = new FXMLLoader(getClass().getResource("merge-sort.fxml"));
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
        return "Mergesort";
    }

    @Override
    public List<Object> getVariables() {
        return List.of("some", "merge", "sort");
    }

    @FXML
    public void initialize() {
        this.label.setText(getName());
    }
}
