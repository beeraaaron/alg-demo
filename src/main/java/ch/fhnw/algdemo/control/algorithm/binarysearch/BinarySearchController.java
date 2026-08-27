package ch.fhnw.algdemo.control.algorithm.binarysearch;

import ch.fhnw.algdemo.control.MainController;
import ch.fhnw.algdemo.model.algorithm.AlgorithmController;
import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import ch.fhnw.algdemo.model.command.BinarySearchCommand;
import ch.fhnw.algdemo.model.command.Command;
import ch.fhnw.algdemo.model.algorithm.IntegerAlgorithmVariable;
import ch.fhnw.algdemo.util.BinarySearchCommandParser;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BinarySearchController extends GridPane implements AlgorithmController {
    private final List<Integer> data = new ArrayList<>(List.of(5, 8, 12, 16, 23, 38, 45, 56, 67, 72, 75, 86, 91, 97));
    private final List<AlgorithmVariable<?>> variables =  List.of(
            new IntegerAlgorithmVariable("i",  null),
            new IntegerAlgorithmVariable("j", null),
            new IntegerAlgorithmVariable("m", null)
    );
    private final List<String> algorithmOptions = List.of(
            "1: i=0, j=n-1",
            "2: i=0, j=n",
            "3: i=-1, j=n-1",
            "4: i=-1, j=n"
    );
    private int selectedAlgorithmOption = 1;
    private final List<Command> commandHistory = new ArrayList<>();
    private final List<BinarySearchColumn> columns = new ArrayList<>();
    private Set<Integer> searchedIndexes = new HashSet<>();
    private int selectedCommandId = 1;
    private int highestCommandId = 1;

    private final MainController mainController;

    @FXML
    HBox columnBox;
    @FXML
    ChoiceBox<String> optionsChoiceBox;
    @FXML
    ComboBox<String> commandInput;
    @FXML
    Button sendCommandButton;

    public BinarySearchController(MainController mainController) {
        this.mainController = mainController;
        loadFxController();
        configureChoiceBox();
        columnBox.prefWidthProperty().bind(this.widthProperty());
    }

    @FXML
    public void initialize() {
        configureSendCommandButton();
        configureCommandInput();
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
    public void onCommandCopied(String commandExpression) {
        commandInput.getEditor().setText(commandExpression);
    }

    @Override
    public void updateAlgorithmState(int selectedCommandId) {
        this.selectedCommandId = selectedCommandId;
        resetAlgorithmState();
        updateStateToSelectedCommand();

        var m = variables.get(2);
        for (var column : columns) {
            var filteredVariables = variables.stream()
                    .filter(v -> column.getIndex().equals(v.getValue()))
                    .map(AlgorithmVariable::getName)
                    .toList();
            column.setVariables(filteredVariables);

            defineColumnColor(column);

            if (m.getValue() != null && searchedIndexes.contains(column.getIndex())) {
                column.enableValueField();
            } else {
                column.disableValueField();
            }
        }

        if (selectedCommandId == 1) {
            for (var column : columns) {
                column.enableValueField();
            }
        }
    }

    @Override
    public int getSelectedCommandId() {
        return highestCommandId;
    }

    private void configureSendCommandButton() {
        sendCommandButton.setOnMouseClicked(event -> sendCommand(null));
        sendCommandButton.setOnKeyPressed(this::sendCommand);
    }

    private void configureCommandInput() {
        commandInput.addEventFilter(KeyEvent.KEY_PRESSED, this::navigateComboBox);
        commandInput.setOnShowing(event -> {
            commandInput.getItems().clear();
            commandInput.getItems().addAll(getCommandSuggestions());
        });
    }

    private void navigateComboBox(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!commandInput.isShowing()) {
                commandInput.show();
            } else {
                commandInput.hide();
            }
            event.consume();
        } else if (event.getCode() == KeyCode.ESCAPE && commandInput.isShowing()) {
            commandInput.hide();
            event.consume();
        }
    }

    private void sendCommand(KeyEvent event) {
        if (event == null || event.getCode() == KeyCode.ENTER) {
            String commandExpression = commandInput.getEditor().getText();
            applyCommand(commandExpression);
            mainController.applyCommand();
            commandInput.getEditor().clear();
        }
    }

    private List<String> getCommandSuggestions() {
        var suggestions = new ArrayList<String>();
        if (selectedAlgorithmOption == 1 || selectedAlgorithmOption == 2) {
            suggestions.add(variables.getFirst().getName() + " = 0");
        } else {
            suggestions.add(variables.getFirst().getName() + " = -1");
        }

        if (selectedAlgorithmOption == 1 || selectedAlgorithmOption == 3) {
            suggestions.add(variables.get(1).getName() + " = " + (data.size() - 1));
        } else {
            suggestions.add(variables.get(1).getName() + " = " + data.size());
        }
        suggestions.add(variables.get(2).getName() + " = (" + variables.getFirst().getName() + " + " + variables.get(1).getName() + ") / 2");
        return suggestions;
    }

    private void applyCommand(String commandExpression) {
        deleteUnsuccessfulCommands();
        try {
            updateStateToSelectedCommand();
            var commandParser = new BinarySearchCommandParser(variables, data);
            var command = commandParser.createCommand(commandExpression);
            if (selectedCommandId < highestCommandId) {
                commandHistory.removeIf(c -> selectedCommandId < c.getId());
                highestCommandId = selectedCommandId + 1;
            } else {
                highestCommandId++;
            }
            command.setId(highestCommandId);
            commandHistory.add(command);
            updateAlgorithmState(highestCommandId);
        } catch (IllegalArgumentException e) {
            if (selectedCommandId < highestCommandId) {
                commandHistory.removeIf(c -> selectedCommandId < c.getId());
                highestCommandId = selectedCommandId;
            }
            commandHistory.add(new BinarySearchCommand(commandExpression, e.getMessage(), false));
        }
    }

    private void defineColumnColor(BinarySearchColumn column) {
        var i = variables.getFirst();
        var j = variables.get(1);

        if ((selectedAlgorithmOption == 1 || selectedAlgorithmOption == 2) &&
                i.getValue() != null && column.getIndex() < (Integer) i.getValue()) {
            column.setMinColor();
        } else if ((selectedAlgorithmOption == 3 || selectedAlgorithmOption == 4) &&
                i.getValue() != null && column.getIndex() <= (Integer) i.getValue()) {
            column.setMinColor();
        } else if ((selectedAlgorithmOption == 1 || selectedAlgorithmOption == 3) &&
                j.getValue() != null && column.getIndex() > (Integer) j.getValue()) {
            column.setMaxColor();
        } else if ((selectedAlgorithmOption == 2 || selectedAlgorithmOption == 4) &&
                j.getValue() != null && column.getIndex() >= (Integer) j.getValue()) {
            column.setMaxColor();
        } else {
            column.removeColors();
        }
    }

    private void deleteUnsuccessfulCommands() {
        commandHistory.removeIf(command -> !((BinarySearchCommand) command).isSuccess());
    }

    private void updateStateToSelectedCommand() {
        searchedIndexes = new HashSet<>();
        var x = 1;
        while (x < selectedCommandId) {
            var command = (BinarySearchCommand) commandHistory.get(x - 1);
            var variable = variables.stream()
                    .filter(v -> v.getName().equals(command.getVariableName()))
                    .findFirst().orElseThrow();
            variable.setValueFromString(command.getValue());
            if (variable.getName().equals(variables.get(2).getName())) {
                searchedIndexes.add((Integer) variable.getValue());
            }
            x++;
        }
    }

    private void resetAlgorithmState() {
        columnBox.getChildren().clear();
        for (var i = 0; i < data.size(); i++) {
            var column = new BinarySearchColumn(data.get(i), i);
            columns.add(column);
            column.enableValueField();
            columnBox.getChildren().add(column);
        }
        for (var variable : variables) {
            variable.setValue(null);
        }
    }

    private void configureChoiceBox() {
        optionsChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> changeAlgorithmOptions(newValue)
        );
        optionsChoiceBox.addEventFilter(KeyEvent.KEY_PRESSED, this::navigateOptionsChoiceBox);
        optionsChoiceBox.setItems(FXCollections.observableArrayList(algorithmOptions));
        optionsChoiceBox.getSelectionModel().select(0);
    }

    private void navigateOptionsChoiceBox(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!optionsChoiceBox.isShowing()) {
                optionsChoiceBox.show();
            } else {
                optionsChoiceBox.hide();
            }
            event.consume();
        } else if (event.getCode() == KeyCode.ESCAPE && optionsChoiceBox.isShowing()) {
            optionsChoiceBox.hide();
            event.consume();
        }
    }

    private void changeAlgorithmOptions(String newValue) {
        this.selectedAlgorithmOption = Integer.parseInt(newValue.split(":")[0]);
        updateAlgorithmState(selectedCommandId);
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("binary-search.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
