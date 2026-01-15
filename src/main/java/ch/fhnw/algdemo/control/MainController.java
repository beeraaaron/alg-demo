package ch.fhnw.algdemo.control;

import ch.fhnw.algdemo.control.variable.VariableController;
import ch.fhnw.algdemo.model.algorithm.AlgorithmController;
import ch.fhnw.algdemo.control.algorithm.binarysearch.BinarySearchController;
import ch.fhnw.algdemo.control.algorithm.mergesort.MergeSortController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.List;

public class MainController {
    @FXML
    private Pane mainGridPane;

    @FXML
    private VBox leftBox;
    @FXML
    private ChoiceBox<AlgorithmController> algorithmChoiceBox;
    @FXML
    private VariableController variableController;
    @FXML
    private VBox variable;

    @FXML
    private Pane midBox;
    @FXML
    private ComboBox<String> commandInput;
    @FXML
    private Button sendCommandButton;

    @FXML
    private VBox rightBox;

    private final List<AlgorithmController> algorithms = List.of(new BinarySearchController(), new MergeSortController());
    private AlgorithmController selectedAlgorithm;

    @FXML
    public void initialize() {
        configureSendCommandButton();
        configureChoiceBox();
        algorithmChoiceBox.prefWidthProperty().bind(leftBox.widthProperty());
        variable.prefWidthProperty().bind(leftBox.widthProperty());
    }

    private void configureSendCommandButton() {
        sendCommandButton.setOnMouseClicked(event -> sendCommand());
    }

    private void configureChoiceBox() {
        algorithmChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(AlgorithmController algorithm) {
                return algorithm == null ? null : algorithm.getName();
            }

            @Override
            public AlgorithmController fromString(String string) {
                return null;
            }
        });
        algorithmChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable,
                 oldValue, newValue) -> {
                    changeAlgorithm(newValue);
                }
        );
        algorithmChoiceBox.setItems(FXCollections.observableArrayList(algorithms));
        algorithmChoiceBox.getSelectionModel().select(0);
    }

    private void sendCommand() {
        String command = commandInput.getEditor().getText();
        var variables = selectedAlgorithm.applyCommand(command);
        variableController.clear();
        variableController.initializeVariables(variables);
        commandInput.getEditor().clear();
    }

    private void changeAlgorithm(AlgorithmController newValue) {
        this.selectedAlgorithm = newValue;

        variableController.clear();
        midBox.getChildren().clear();
        commandInput.getItems().clear();
        if (newValue instanceof BinarySearchController bsc) {
            variableController.initializeVariables(bsc.getVariables());
            bsc.prefWidthProperty().bind(midBox.widthProperty());
            midBox.getChildren().addAll(bsc);
            commandInput.getItems().addAll(bsc.getCommandSuggestions());
        } else if (newValue instanceof MergeSortController msc) {
            variableController.initializeVariables(msc.getVariables());
            msc.prefWidthProperty().bind(midBox.widthProperty());
            midBox.getChildren().addAll(msc);
            commandInput.getItems().addAll(msc.getCommandSuggestions());
        }
    }
}
