package ch.fhnw.algdemo.control.history;

import ch.fhnw.algdemo.model.command.Command;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.function.Consumer;

public class HistoryElement extends GridPane {
    @FXML
    private GridPane historyElement;
    @FXML
    private Label commandLabel;
    @FXML
    private Label resultLabel;
    @FXML
    private Button copyButton;

    @Setter
    private Consumer<String> onCommandCopied;
    @Setter
    private Consumer<Integer> onStateClicked;
    private final Command command;
    private final boolean isSelected;

    public HistoryElement(Command command, boolean isSelected) {
        this.command = command;
        this.isSelected = isSelected;
        loadFxController();
    }

    @FXML
    public void initialize() {
        this.commandLabel.setText(">> " + command.command);
        this.resultLabel.setText(command.result);
        if (!command.success) {
            configureError();
        } else {
            configureCopyButton();
            configureChangeState();
            historyElement.getStyleClass().add("clickable-history-element");
        }
        if (isSelected) configureSelected();
    }

    private void configureCopyButton() {
        copyButton.setOnMouseClicked(event -> copyCommand(null));
        copyButton.addEventFilter(KeyEvent.KEY_PRESSED, this::copyCommand);
    }

    private void configureChangeState() {
        historyElement.setFocusTraversable(true);
        historyElement.setOnMouseClicked(event -> changeState(null));
        historyElement.addEventFilter(KeyEvent.KEY_PRESSED, this::changeState);
    }

    private void configureError() {
        resultLabel.getStyleClass().add("error-message");
        copyButton.setVisible(false);
    }

    private void configureSelected() {
        historyElement.getStyleClass().add("element-selected");
    }

    private void copyCommand(KeyEvent keyEvent) {
        if (keyEvent == null || keyEvent.getCode() == KeyCode.ENTER) {
            onCommandCopied.accept(command.command);
        }
    }

    private void changeState(KeyEvent keyEvent) {
        if (keyEvent == null || keyEvent.getCode() == KeyCode.ENTER) {
            onStateClicked.accept(command.id);
            if (keyEvent != null) {
                keyEvent.consume();
            }
            historyElement.requestFocus();
        }
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("history-element.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
