package ch.fhnw.algdemo.control.history;

import ch.fhnw.algdemo.model.algorithm.Command;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class HistoryController extends ScrollPane {
    @FXML
    private VBox historyBox;

    List<Command> commandHistory;
    @Setter
    private Consumer<String> onCommandCopied;

    public void initializeHistory(List<Command> commandHistory) {
        this.commandHistory = commandHistory;
        for (var command : this.commandHistory) {
            var historyElement = new HistoryElement(command);
            historyElement.prefWidthProperty().bind(historyBox.widthProperty());
            historyElement.setOnCommandCopied(c -> onCommandCopied.accept(c));
            historyBox.getChildren().add(historyElement);
        }
    }

    public void clear() {
        historyBox.getChildren().clear();
    }
}