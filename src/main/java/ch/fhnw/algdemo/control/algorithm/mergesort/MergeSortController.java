package ch.fhnw.algdemo.control.algorithm.mergesort;

import ch.fhnw.algdemo.model.algorithm.AlgorithmController;
import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import ch.fhnw.algdemo.model.command.Command;
import ch.fhnw.algdemo.model.algorithm.IntegerAlgorithmVariable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import lombok.SneakyThrows;

import java.util.List;

public class MergeSortController extends GridPane implements AlgorithmController {
    private final List<AlgorithmVariable<?>> variables = List.of(
            new IntegerAlgorithmVariable("someName", null)
    );

    public MergeSortController() {
        loadFxController();
    }

    @FXML
    public void initialize() {

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
    public void updateAlgorithmState(int selectedCommandId) {
    }

    @Override
    public int getHighestCommandId() {
        return 0;
    }

    @Override
    public void onCommandCopied(String commandExpression) {}
}
