package ch.fhnw.algdemo.control.algorithm.mergesort;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.SneakyThrows;

public class MergeSortColumn extends GridPane {
    private final SimpleIntegerProperty number;
    private final SimpleBooleanProperty visibility;
    private final SimpleBooleanProperty comparison;
    private final SimpleBooleanProperty overwrite;
    private final SimpleBooleanProperty editable;

    @FXML
    TextField valueField;

    public MergeSortColumn(SimpleIntegerProperty number, SimpleBooleanProperty visibility,
                           SimpleBooleanProperty comparison, SimpleBooleanProperty overwrite, boolean isEditable) {
        this.number = number;
        this.visibility = visibility;
        this.comparison = comparison;
        this.overwrite = overwrite;
        this.editable = new SimpleBooleanProperty(isEditable);
        loadFxController();
    }

    @FXML
    public void initialize() {
        configureValueField();
        configureComparisonHighlighting();
        configureOverwriteHighlighting();
    }

    public void enableValueField() {
        editable.set(true);
    }

    public void disableValueField() {
        editable.set(false);
    }

    private void configureValueField() {
        valueField.visibleProperty().bind(this.visibility);
        valueField.managedProperty().bind(this.visibility);
        valueField.disableProperty().bind(this.editable.not());
        if (editable.getValue()) {
            this.number.addListener((obs, oldVal, newVal) -> {
                if (!valueField.isFocused()) {
                    valueField.setText(newVal.toString());
                }
            });
            valueField.setOnAction(e -> commitValue());
            valueField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                if (!isFocused) {
                    commitValue();
                }
            });
            valueField.setText(String.valueOf(this.number.get()));
        } else {
            valueField.textProperty().bind(this.number.asString());
        }
    }

    private void commitValue() {
        try {
            int i = Integer.parseInt(valueField.getText());
            if (i >= 0 && i < 100) {
                number.set(i);
            }
        } catch (NumberFormatException e) {
            // ignore, fall through to reset
        }
        valueField.setText(String.valueOf(number.get()));
    }

    private void configureComparisonHighlighting() {
        comparison.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                valueField.getStyleClass().add("cell-comparison");
            } else {
                valueField.getStyleClass().remove("cell-comparison");
            }
        });
    }

    private void configureOverwriteHighlighting() {
        overwrite.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                valueField.getStyleClass().add("cell-overwrite");
            } else {
                valueField.getStyleClass().remove("cell-overwrite");
            }
        });
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("merge-sort-column.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
