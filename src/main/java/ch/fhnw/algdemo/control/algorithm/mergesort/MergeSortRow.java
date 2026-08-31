package ch.fhnw.algdemo.control.algorithm.mergesort;

import ch.fhnw.algdemo.model.command.MergeSortCommand;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class MergeSortRow extends GridPane {
    private final SimpleObjectProperty<MergeSortCommand> command;
    private final int length;
    private final int rowDepth;

    private final List<MergeSortColumn> mergeSortColumns = new ArrayList<>();

    private SimpleIntegerProperty[] numberProperties;
    private SimpleBooleanProperty[] visibilityProperties;
    private SimpleBooleanProperty[] comparisonProperties;
    private SimpleBooleanProperty[] overwriteProperties;

    private final ChangeListener<MergeSortCommand> commandListener;
    private BiConsumer<Integer, Integer> onNumberChanged;
    private List<ChangeListener<Number>> numberPropertyListeners;

    @FXML
    GridPane rootGridPane;

    public MergeSortRow(SimpleObjectProperty<MergeSortCommand> command, int length, int rowDepth) {
        this.command = command;
        this.length = length;
        this.rowDepth = rowDepth;

        initializeProperties();
        loadFxController();
        initializeRow();

        if (rowDepth == 0) {
            rootGridPane.getStyleClass().add("border-bottom");
        }

        this.commandListener = (obs, oldCmd, newCmd) -> applyCommand(newCmd);
        this.command.addListener(this.commandListener);
        applyCommand(this.command.getValue());
    }

    private void initializeRow() {
        List<int[]> ranges = splitRanges(0, length, rowDepth);
        double rangeGap = computeRangeGap(length);

        var percentWidth = 100.0 / length;
        for (int i = 0; i < length; i++) {
            var cc = new ColumnConstraints();
            cc.setPercentWidth(percentWidth);
            rootGridPane.getColumnConstraints().add(cc);
        }

        for (int[] range : ranges) {
            int beg = range[0];
            int end = range[1];
            var container = new HBox();
            container.setSpacing(0);
            container.setFillHeight(true);
            for (int j = beg; j < end; j++) {
                var col = new MergeSortColumn(numberProperties[j], visibilityProperties[j],
                        comparisonProperties[j], overwriteProperties[j], rowDepth == 0);

                HBox.setHgrow(col, Priority.ALWAYS);
                col.setMaxWidth(Double.MAX_VALUE);
                mergeSortColumns.add(col);
                container.getChildren().add(col);
            }
            double left = rangeGap / 2;
            double right = rangeGap / 2;
            GridPane.setMargin(container, new Insets(0, right, 0, left));

            rootGridPane.add(container, beg, 0, end - beg, 1);
        }
    }

    private List<int[]> splitRanges(int beg, int end, int depth) {
        if (depth == 0 || end - beg <= 1) {
            return new ArrayList<>(List.of(new int[]{beg, end}));
        }
        int m = (beg + end) / 2;
        var result = new ArrayList<>(splitRanges(beg, m, depth - 1));
        result.addAll(splitRanges(m, end, depth - 1));
        return result;
    }

    private static double computeRangeGap(int length) {
        double slope = (15.0 - 40.0) / (16.0 - 4.0);
        double gap = 40.0 + slope * (length - 4);
        return Math.max(gap, 15);
    }

    public void setOnNumberChanged(BiConsumer<Integer, Integer> callback) {
        this.onNumberChanged = callback;

        this.numberPropertyListeners = new ArrayList<>();
        for (int i = 0; i < this.length; i++) {
            final int index = i;
            ChangeListener<Number> listener = (obs, oldVal, newVal) -> {
                if (this.onNumberChanged != null) {
                    this.onNumberChanged.accept(index, newVal.intValue());
                }
            };
            this.numberProperties[i].addListener(listener);
            this.numberPropertyListeners.add(listener);
        }
    }

    public void detachListener() {
        if (this.commandListener != null) {
            this.command.removeListener(this.commandListener);
        }

        if (this.numberPropertyListeners != null) {
            for (int i = 0; i < this.length; i++) {
                this.numberProperties[i].removeListener(this.numberPropertyListeners.get(i));
            }
            this.numberPropertyListeners.clear();
        }
    }

    public void changeValueFieldDisability(boolean b) {
        if (rowDepth == 0) {
            for (var col : mergeSortColumns) {
                if (b) {
                    col.enableValueField();
                } else {
                    col.disableValueField();
                }
            }
        }
    }

    private void applyCommand(MergeSortCommand cmd) {
        if (cmd == null) return;
        var numbers = cmd.getNumbers().get(rowDepth);
        var visibilities = cmd.getVisibilities().get(rowDepth);
        var comparisons = cmd.getComparisons().get(rowDepth);
        var overwrites = cmd.getOverwrites().get(rowDepth);

        for (int i = 0; i < length; i++) {
            numberProperties[i].set(numbers.get(i));
            visibilityProperties[i].set(visibilities.get(i));
            comparisonProperties[i].set(comparisons.get(i));
            overwriteProperties[i].set(overwrites.get(i));
        }
    }

    private void initializeProperties() {
        this.numberProperties = new SimpleIntegerProperty[length];
        this.visibilityProperties = new SimpleBooleanProperty[length];
        this.comparisonProperties = new SimpleBooleanProperty[length];
        this.overwriteProperties = new SimpleBooleanProperty[length];
        for (int i = 0; i < length; i++) {
            numberProperties[i] = new SimpleIntegerProperty();
            visibilityProperties[i] = new SimpleBooleanProperty();
            comparisonProperties[i] = new SimpleBooleanProperty();
            overwriteProperties[i] = new SimpleBooleanProperty();
        }
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("merge-sort-row.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
