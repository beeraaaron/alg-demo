package ch.fhnw.algdemo.control;

import ch.fhnw.algdemo.control.history.HistoryController;
import ch.fhnw.algdemo.control.variable.VariableController;
import ch.fhnw.algdemo.model.algorithm.AlgorithmController;
import ch.fhnw.algdemo.control.algorithm.binarysearch.BinarySearchController;
import ch.fhnw.algdemo.control.algorithm.mergesort.MergeSortController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

import java.util.List;

public class MainController {
    @FXML
    VBox leftBox;
    @FXML
    ChoiceBox<AlgorithmController> algorithmChoiceBox;
    @FXML
    VariableController variableController;
    @FXML
    VBox variable;
    @FXML
    VBox midBox;
    @FXML
    HistoryController historyController;

    private final List<AlgorithmController> algorithms = List.of(
            new BinarySearchController(this), new MergeSortController(this));
    private AlgorithmController selectedAlgorithm;

    @FXML
    public void initialize() {
        configureHistory();
        configureChoiceBox();
        variable.prefWidthProperty().bind(leftBox.widthProperty());
    }

    private void configureHistory() {
        historyController.setOnCommandCopied(command -> selectedAlgorithm.onCommandCopied(command));
        historyController.setOnStateClicked(id -> {
            selectedAlgorithm.updateAlgorithmState(id);
            variableController.clear();
            variableController.initializeVariables(selectedAlgorithm.getVariables());
        });
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
                 oldValue, newValue) -> changeAlgorithm(newValue)
        );
        algorithmChoiceBox.setItems(FXCollections.observableArrayList(algorithms));
        algorithmChoiceBox.addEventFilter(KeyEvent.KEY_PRESSED, this::navigateChoiceBox);
        algorithmChoiceBox.prefWidthProperty().bind(leftBox.widthProperty());
        algorithmChoiceBox.getSelectionModel().select(1);
    }

    private void navigateChoiceBox(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!algorithmChoiceBox.isShowing()) {
                algorithmChoiceBox.show();
            } else {
                algorithmChoiceBox.hide();
            }
            event.consume();
        } else if (event.getCode() == KeyCode.ESCAPE && algorithmChoiceBox.isShowing()) {
            algorithmChoiceBox.hide();
            event.consume();
        }
    }

    private void changeAlgorithm(AlgorithmController newValue) {
        this.selectedAlgorithm = newValue;
        variableController.clear();
        midBox.getChildren().clear();
        if (newValue instanceof BinarySearchController bsc) {
            variableController.initializeVariables(bsc.getVariables());
            historyController.initializeHistory(bsc.getCommandHistory(), bsc.getHighestCommandId());
            midBox.getChildren().addAll(bsc);
            bsc.prefWidthProperty().bind(midBox.widthProperty());
            bsc.prefHeightProperty().bind(midBox.heightProperty());
        } else if (newValue instanceof MergeSortController msc) {
            variableController.initializeVariables(msc.getVariables());
            historyController.initializeHistory(msc.getCommandHistory(), msc.getHighestCommandId());
            midBox.getChildren().addAll(msc);
            msc.prefWidthProperty().bind(midBox.widthProperty());
            msc.prefHeightProperty().bind(midBox.heightProperty());
        }
    }

    public void applyCommand() {
        variableController.clear();
        variableController.initializeVariables(selectedAlgorithm.getVariables());
        historyController.initializeHistory(selectedAlgorithm.getCommandHistory(), selectedAlgorithm.getHighestCommandId());
    }
}
