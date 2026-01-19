package ch.fhnw.algdemo.control.history;

import ch.fhnw.algdemo.model.algorithm.Command;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.function.Consumer;

public class HistoryElement extends GridPane {
    @FXML
    private Label commandLabel;
    @FXML
    private Label resultLabel;
    @FXML
    private Button copyButton;

    private final Command command;
    @Setter
    private Consumer<String> onCommandCopied;


    public HistoryElement(Command command) {
        this.command = command;
        loadFxController();
    }

    @FXML
    public void initialize() {
        this.commandLabel.setText(">> " + command.command);
        this.resultLabel.setText(command.result);
        copyButton.setOnMouseClicked(event -> submitCommand());
        if (!command.success) {
            configureError();
        }
    }

    private void submitCommand() {
        onCommandCopied.accept(command.command);
    }

    public void configureError() {
        resultLabel.getStyleClass().add("error-message");
        copyButton.setVisible(false);
    }

    private void configureCopyButton() {
    }


    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("history-element.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
