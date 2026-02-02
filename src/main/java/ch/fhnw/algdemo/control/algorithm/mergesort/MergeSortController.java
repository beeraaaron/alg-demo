package ch.fhnw.algdemo.control.algorithm.mergesort;

import ch.fhnw.algdemo.model.algorithm.AlgorithmController;
import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import ch.fhnw.algdemo.model.algorithm.Command;
import ch.fhnw.algdemo.model.algorithm.IntegerAlgorithmVariable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.SneakyThrows;

import java.util.List;

public class MergeSortController extends Pane implements AlgorithmController {
    @FXML
    private Label label;

    private List<AlgorithmVariable<?>> variables = List.of(
            new IntegerAlgorithmVariable("someName", null)
    );

    public MergeSortController() {
        loadFxController();
    }

    @FXML
    public void initialize() {
        this.label.setText(getName());
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("merge-sort.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @Override
    public String getName() {
        return "Mergesort";
    }

    @Override
    public List<AlgorithmVariable<?>> getVariables() {
        return variables;
    }

    @Override
    public List<Command> getCommandHistory() {
        return List.of();
    }

    @Override
    public List<String> getCommandSuggestions() {
        return variables.stream()
                .map(v -> v.name + " = 0")
                .toList();
    }

    @Override
    public void applyCommand(String command) {
        return;
    }

    @Override
    public void updateAlgorithmState(int selectedCommandId) {
        return;
    }

    @Override
    public int getHighestCommandId() {
        return 0;
    }
}
