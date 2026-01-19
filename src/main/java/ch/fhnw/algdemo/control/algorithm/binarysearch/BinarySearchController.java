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

public class BinarySearchController extends HBox implements AlgorithmController {
    List<Integer> data = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));
    List<AlgorithmVariable<?>> variables =  List.of(
            new AlgorithmVariable<>("i", Integer.class, null),
            new AlgorithmVariable<>("j", Integer.class, null),
            new AlgorithmVariable<>("m", Integer.class, null)
    );

    List<Command> commandHistory = new ArrayList<>();

    List<BinarySearchColumn> columns = new ArrayList<>();

    public BinarySearchController() {
        loadFxController();
    }

    @FXML
    public void initialize() {
        resetAlgorithmState();
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
    public List<Command> getCommandHistory() {
        return commandHistory;
    }

    @Override
    public List<String> getCommandSuggestions() {
        return variables.stream()
                .map(v -> v.name + " = 0;")
                .toList();
    }

    @Override
    public void applyCommand(String command) {
        deleteUnsuccessfulCommands();
        try {
            var validCommand = createCommand(command);
            commandHistory.add(validCommand);
            updateAlgorithmState();
        } catch (IllegalArgumentException e) {
            commandHistory.add(new Command(command, e.getMessage(), false));
        }
    }

    private void deleteUnsuccessfulCommands() {
        this.commandHistory.removeIf(command -> !command.success);
    }

    private void updateAlgorithmState() {
        resetAlgorithmState();
        for (var command : commandHistory) {
            var variable = variables.stream()
                    .filter(v -> v.name.equals(command.variableName))
                    .findFirst().orElseThrow();
            // TODO determine new value of variable if its not just a number
            variable.setValueFromObject(Integer.parseInt(command.value));
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

    private void resetAlgorithmState() {
        this.getChildren().clear();
        for (var i = 0; i < data.size(); i++) {
            var column = new BinarySearchColumn(data.get(i), i);
            columns.add(column);
            this.getChildren().add(column);
        }
    }

    private Command createCommand(String command) throws IllegalArgumentException {
        String error = null;
        if (!command.contains("=")) {
            throw new IllegalArgumentException("Command must contain '='");
        }
        if (!command.contains(";")) {
            throw new IllegalArgumentException("Command must contain ';' at the end");
        }

        var parts = command.split("=");
        var variableName = parts[0].trim();
        var optionalVariable = variables.stream().filter(v -> v.name.equals(variableName)).findFirst();
        if (variableName.isBlank() || optionalVariable.isEmpty()) {
            var variableNames = variables.stream().map(v -> v.name).toList();
            throw new IllegalArgumentException("Invalid variable '" + variableName + "'. Must be one of: " + variableNames);
        }

        var value = parts[1].split(";")[0].trim();
        var variable = optionalVariable.get();

        //TODO: if value is a number validate correctness
        // if it's not a number, try to evaluate condition and then check correctness

        if (value.isBlank() || !isInt(value)) {
            throw new IllegalArgumentException("Invalid value '" + value + "'. Must be of same type as variable " + variable.name);
        }
        return new Command(command.trim(), variable.name + " = " + value, variable.name,  value, true);
    }

    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
