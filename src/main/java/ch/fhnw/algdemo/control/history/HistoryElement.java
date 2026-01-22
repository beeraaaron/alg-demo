package ch.fhnw.algdemo.control.history;

import ch.fhnw.algdemo.model.algorithm.Command;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.function.Consumer;

public class HistoryElement extends GridPane {
    @FXML
    private GridPane historyElement;
    @FXML
    private VBox detailsBox;
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
    private boolean isSelected;


    public HistoryElement(Command command, boolean isSelected) {
        this.command = command;
        this.isSelected = isSelected;
        loadFxController();
    }

    @FXML
    public void initialize() {
        configureCopyButton();
        detailsBox.setFocusTraversable(true);
        detailsBox.setOnMouseClicked(event -> {
            changeState(null);
        });
        detailsBox.addEventFilter(KeyEvent.KEY_PRESSED, this::changeState);
        this.commandLabel.setText(">> " + command.command);
        this.resultLabel.setText(command.result);
        if (!command.success) {
            configureError();
        }
        if (isSelected) {
            configureSelected();
        }
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
            detailsBox.requestFocus();
        }
    }

    public void configureError() {
        resultLabel.getStyleClass().add("error-message");
        copyButton.setVisible(false);
    }

    private void configureSelected() {
        historyElement.getStyleClass().add("element-selected");
    }

    private void configureCopyButton() {
        copyButton.setOnMouseClicked(event -> copyCommand(null));
        copyButton.addEventFilter(KeyEvent.KEY_PRESSED, this::copyCommand);
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("history-element.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
