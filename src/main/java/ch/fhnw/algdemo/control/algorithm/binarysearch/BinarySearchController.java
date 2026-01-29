package ch.fhnw.algdemo.control.algorithm.binarysearch;

import ch.fhnw.algdemo.model.algorithm.AlgorithmController;
import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import ch.fhnw.algdemo.model.algorithm.Command;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

import static ch.fhnw.algdemo.util.CommandFactory.createCommand;

public class BinarySearchController extends HBox implements AlgorithmController {
    List<Integer> data = new ArrayList<>(List.of(5, 8, 12, 16, 23, 38, 45, 56, 67, 72, 75, 86, 91, 97));
    List<AlgorithmVariable<?>> variables =  List.of(
            new AlgorithmVariable<>("i", Integer.class, null),
            new AlgorithmVariable<>("j", Integer.class, null),
            new AlgorithmVariable<>("m", Integer.class, null)
    );

    List<Command> commandHistory = new ArrayList<>();
    List<BinarySearchColumn> columns = new ArrayList<>();
    int highestCommandId = 2;

    public BinarySearchController() {
        loadFxController();
    }

    @FXML
    public void initialize() {
        resetAlgorithmState();
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
    public List<Command> getCommandHistory() {
        return commandHistory;
    }

    @Override
    public List<String> getCommandSuggestions() {
        return List.of(variables.getFirst().name + " = 0;",
                variables.get(1).name + " = " + (data.size() - 1) + ";",
                variables.get(2).name + " = " + (data.size() / 2) + ";");
    }

    @Override
    public void applyCommand(String command) {
        deleteUnsuccessfulCommands();
        try {
            var validCommand = createCommand(command, variables, this::getNextId);
            commandHistory.add(validCommand);
            updateAlgorithmState(highestCommandId);
        } catch (IllegalArgumentException e) {
            commandHistory.add(new Command(command, e.getMessage(), false));
        }
    }

    @Override
    public void updateAlgorithmState(int selectedCommandId) {
        resetAlgorithmState();
        var x = 1;
        while (x + 1 < selectedCommandId) {
            var command =  commandHistory.get(x - 1);
            var variable = variables.stream()
                    .filter(v -> v.name.equals(command.variableName))
                    .findFirst().orElseThrow();
            variable.setValueFromString(command.value);
            x++;
        }
        var i = variables.getFirst();
        var j = variables.get(1);
        for (var column : columns) {
            var filteredVariables = variables.stream()
                    .filter(v -> column.getIndex().equals(v.value))
                    .toList();
            if (!filteredVariables.isEmpty()) {
                var variables = filteredVariables.stream().map(v -> v.name).toList();
                column.setVariables(variables);
            } else {
                column.setVariables(List.of());
            }

            if (i.value != null && column.getIndex() < (Integer) i.value) {
                column.setMinColor();
            } else if (j.value != null && column.getIndex() >= (Integer) j.value) {
                column.setMaxColor();
            } else {
                column.removeColors();
            }
        }
    }

    @Override
    public int getHighestCommandId() {
        return highestCommandId;
    }

    private void deleteUnsuccessfulCommands() {
        commandHistory.removeIf(command -> !command.success);
    }

    private void resetAlgorithmState() {
        this.getChildren().clear();
        for (var i = 0; i < data.size(); i++) {
            var column = new BinarySearchColumn(data.get(i), i);
            columns.add(column);
            this.getChildren().add(column);
        }
        for (var variable : variables) {
            variable.value = null;
        }
    }

    public int getNextId() {
        highestCommandId++;
        return highestCommandId;
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("binary-search.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
