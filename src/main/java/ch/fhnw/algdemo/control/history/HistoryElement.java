package ch.fhnw.algdemo.control.history;

import ch.fhnw.algdemo.model.command.BinarySearchCommand;
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
    GridPane historyElement;
    @FXML
    Label commandLabel;
    @FXML
    Label resultLabel;
    @FXML
    Button copyButton;

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
        commandLabel.setText(">> " + command.getCommand());
        historyElement.getStyleClass().add("clickable-history-element");
        configureChangeState();
        configureCopyButton(command);
        if (command instanceof BinarySearchCommand bsc) {
            if (!bsc.isSuccess()) {
                configureError();
            }
            this.resultLabel.setText(bsc.getResult());
        }
        if (isSelected) configureSelected();
    }

    private void configureCopyButton(Command command) {
        copyButton.setManaged(command instanceof BinarySearchCommand);
        copyButton.setVisible(command instanceof BinarySearchCommand);
        if (command instanceof BinarySearchCommand) {
            copyButton.setOnMouseClicked(event -> copyCommand(null));
            copyButton.addEventFilter(KeyEvent.KEY_PRESSED, this::copyCommand);
        }
    }

    private void configureChangeState() {
        historyElement.setFocusTraversable(true);
        historyElement.setOnMouseClicked(event -> changeState(null));
        historyElement.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                changeState(event);
            }
        });
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
            onCommandCopied.accept(command.getCommand());
            if (keyEvent != null) {
                keyEvent.consume();
            }
        }
    }

    private void changeState(KeyEvent keyEvent) {
        if (keyEvent == null || keyEvent.getCode() == KeyCode.ENTER) {
            onStateClicked.accept(command.getId());
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
