package ch.fhnw.algdemo.control.algorithm.binarysearch;

import ch.fhnw.algdemo.model.algorithm.AlgorithmController;
import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchController extends HBox implements AlgorithmController {
    List<AlgorithmVariable<?>> variables =  List.of(
            new AlgorithmVariable<>("i", Integer.class, null),
            new AlgorithmVariable<>("j", Integer.class, null),
            new AlgorithmVariable<>("m", Integer.class, null)
    );

    List<Integer> data = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));


    public BinarySearchController() {
        loadFxController();
    }

    @FXML
    public void initialize() {
        for (var i = 0; i < data.size(); i++) {
            var column = new BinarySearchColumn(data.get(i), i, "");
            this.getChildren().add(column);
        }
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("binary-search.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @Override
    public String getName() {
        return "Binary Search";
    }

    @Override
    public List<AlgorithmVariable<?>> getVariables() {
        return variables;
    }

    @Override
    public List<String> getCommandSuggestions() {
        return variables.stream()
                .map(v -> v.name + " = 0;")
                .toList();
    }

    @Override
    public List<AlgorithmVariable<?>> applyCommand(String command) {
        var parts = command.split("=");
        var variableName = parts[0].trim();
        var variableValue = Integer.parseInt(parts[1].split(";")[0].trim());
        var optionalVariable = variables.stream().filter(v -> v.name.equals(variableName)).findAny();
        optionalVariable.ifPresent(variable -> variable.setValueFromObject(variableValue));
        return variables;
    }
}
