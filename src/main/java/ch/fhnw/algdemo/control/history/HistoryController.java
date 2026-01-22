package ch.fhnw.algdemo.control.history;

import ch.fhnw.algdemo.model.algorithm.Command;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.util.List;
import java.util.function.Consumer;

public class HistoryController extends ScrollPane {
    @FXML
    private VBox historyBox;
    @FXML
    private Label initialStateLabel;
    @FXML
    private ScrollPane scrollPane;

    @Setter
    private Consumer<String> onCommandCopied;
    @Setter
    private Consumer<Integer> onStateClicked;
    List<Command> commandHistory;

    @FXML
    public void initialize() {
        historyBox.heightProperty().addListener((ob,ov,nv) -> {
            scrollPane.setVvalue(1.0);
        });
        initialStateLabel.setFocusTraversable(true);
        initialStateLabel.setOnMouseClicked(event -> changeState(null));
        initialStateLabel.addEventFilter(KeyEvent.KEY_PRESSED, this::changeState);
    }

    private void changeState(KeyEvent keyEvent) {
        if (keyEvent == null || keyEvent.getCode() == KeyCode.ENTER) {
            onStateClicked.accept(1);
            clear();
            initializeHistory(commandHistory, 1);
            if (keyEvent != null) {
                keyEvent.consume();
            }
            initialStateLabel.requestFocus();
        }
    }

    public void initializeHistory(List<Command> commandHistory, int selectedCommandId) {
        this.commandHistory = commandHistory;
        if (commandHistory.isEmpty() || selectedCommandId == 1) {
            initialStateLabel.getStyleClass().add("element-selected");
        } else {
            initialStateLabel.getStyleClass().remove("element-selected");
        }

        for (var command : this.commandHistory) {
            var historyElement = new HistoryElement(command, command.id == selectedCommandId);
            historyElement.prefWidthProperty().bind(historyBox.widthProperty());
            historyElement.setOnCommandCopied(c -> onCommandCopied.accept(c));
            historyElement.setOnStateClicked(id -> {
                onStateClicked.accept(id);
                clear();
                initializeHistory(commandHistory, id);
            });
            historyBox.getChildren().add(historyElement);
        }
    }

    public void clear() {
        historyBox.getChildren().clear();
    }
}