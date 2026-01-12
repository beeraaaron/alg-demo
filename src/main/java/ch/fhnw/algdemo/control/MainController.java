package ch.fhnw.algdemo.control;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;

import java.util.List;

public class MainController {
    private final List<AlgorithmController> algorithms = List.of(new BinarySearchController(), new MergeSortController());

    @FXML
    private Pane mainPane;

    @FXML
    private ChoiceBox<AlgorithmController> algorithmChoiceBox;

    @FXML
    public void initialize() {
        initializeAlgorithmChoiceBox();
        var variableController = new VariableController(List.of());
        mainPane.getChildren().add(variableController);
    }

    private void initializeAlgorithmChoiceBox() {
        algorithmChoiceBox.setConverter(new javafx.util.StringConverter<AlgorithmController>() {
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

    public void changeAlgorithm(AlgorithmController newValue) {
        mainPane.getChildren().clear();
        if (newValue instanceof BinarySearchController bsc) {
            bsc.setLayoutY(500);
            bsc.setLayoutX(500);
            var variableController = new VariableController(bsc.getVariables());
            variableController.setLayoutY(100);
            mainPane.getChildren().addAll(algorithmChoiceBox, variableController, bsc);
        } else if (newValue instanceof MergeSortController msc) {
            msc.setLayoutY(500);
            msc.setLayoutX(500);
            var variableController = new VariableController(msc.getVariables());
            variableController.setLayoutY(100);
            mainPane.getChildren().addAll(algorithmChoiceBox, variableController, msc);
        }
    }
}
