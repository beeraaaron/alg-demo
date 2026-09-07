package ch.fhnw.algdemo.control.algorithm.mergesort;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.SneakyThrows;

public class MergeSortColumn extends GridPane {
    private final SimpleObjectProperty<Integer> number;
    private final SimpleStringProperty index;
    private final SimpleBooleanProperty visibility;
    private final SimpleBooleanProperty comparison;
    private final SimpleBooleanProperty overwrite;
    private final SimpleBooleanProperty editable;

    private boolean isLabelOnlyColumn = false;

    @FXML
    TextField valueField;
    @FXML
    Label indexLabel;

    public MergeSortColumn(SimpleObjectProperty<Integer> number, SimpleStringProperty index, SimpleBooleanProperty visibility,
                           SimpleBooleanProperty comparison, SimpleBooleanProperty overwrite, boolean isEditable) {
        this.number = number;
        this.index = index;
        this.visibility = visibility;
        this.comparison = comparison;
        this.overwrite = overwrite;
        this.editable = new SimpleBooleanProperty(isEditable);
        loadFxController();
    }

    public MergeSortColumn(SimpleStringProperty index) {
        this.isLabelOnlyColumn = true;
        this.index = index;
        this.number = new SimpleObjectProperty<>();
        this.visibility = new SimpleBooleanProperty();
        this.comparison = new SimpleBooleanProperty();
        this.overwrite = new SimpleBooleanProperty();
        this.editable = new SimpleBooleanProperty();
        loadFxController();
    }

    @FXML
    public void initialize() {
        this.indexLabel.textProperty().bind(this.index);
        if (!isLabelOnlyColumn) {
            configureValueField();
            configureComparisonHighlighting();
            configureOverwriteHighlighting();
        } else  {
            valueField.setVisible(false);
        }
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
                    valueField.setText(newVal == null ? "" : newVal.toString());
                }
            });
            valueField.setOnAction(e -> commitValue());
            valueField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                if (!isFocused) {
                    commitValue();
                }
            });
            valueField.setText(this.number.get() == null ? "" : this.number.get().toString());
        } else {
            valueField.textProperty().bind(Bindings.createStringBinding(() ->
                    this.number.get() == null ? "" : this.number.get().toString(), this.number));
        }
    }

    private void commitValue() {
        try {
            String txt = valueField.getText();
            if (txt == null || txt.isBlank()) {
                number.set(null);
            } else {
                int i = Integer.parseInt(txt);
                if (i >= 0 && i < 100) {
                    number.set(i);
                }
            }
        } catch (NumberFormatException e) {
            // ignore, fall through to reset
        }
        valueField.setText(number.get() == null ? "" : number.get().toString());
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
