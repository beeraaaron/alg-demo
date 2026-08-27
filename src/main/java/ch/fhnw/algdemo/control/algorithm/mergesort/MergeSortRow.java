package ch.fhnw.algdemo.control.algorithm.mergesort;

import ch.fhnw.algdemo.model.command.MergeSortCommand;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class MergeSortRow extends GridPane {
    private final SimpleObjectProperty<MergeSortCommand> command;
    private final int length;
    private final int rowDepth;
    private final int maxDepth;

    private final SimpleIntegerProperty[] numberProperties;
    private final SimpleBooleanProperty[] visibilityProperties;
    private final SimpleBooleanProperty[] highlightProperties;

    @FXML
    GridPane rootGridPane;

    public MergeSortRow(SimpleObjectProperty<MergeSortCommand> command, int length, int rowDepth, int maxDepth) {
        this.command = command;
        this.length = length;
        this.rowDepth = rowDepth;
        this.maxDepth = maxDepth;

        this.numberProperties = new SimpleIntegerProperty[length];
        this.visibilityProperties = new SimpleBooleanProperty[length];
        this.highlightProperties = new SimpleBooleanProperty[length];
        for (int i = 0; i < length; i++) {
            numberProperties[i] = new SimpleIntegerProperty();
            visibilityProperties[i] = new SimpleBooleanProperty();
            highlightProperties[i] = new SimpleBooleanProperty();
        }

        loadFxController();
        initializeRow();

        this.command.addListener((obs, oldCmd, newCmd) -> applyCommand(newCmd));
        applyCommand(this.command.getValue());
    }

    private void initializeRow() {
        List<int[]> ranges = splitRanges(0, length, rowDepth);

        int columnAmount = length * 3;
        var percentWidth = 100.0 / columnAmount;
        for (int i = 0; i < columnAmount; i++) {
            var cc = new ColumnConstraints();
            cc.setPercentWidth(percentWidth);
            rootGridPane.getColumnConstraints().add(cc);
        }

        for (int[] range : ranges) {
            int beg = range[0];
            int end = range[1];
            boolean visible = range[2] == 1;
            int column = 3 * beg + (end - beg);
            for (int j = beg; j < end; j++) {
                if (visible) {
                    var col = new MergeSortColumn(numberProperties[j], visibilityProperties[j], highlightProperties[j]);
                    rootGridPane.add(col, column, 0);
                }
                column++;
            }
        }
    }

    private List<int[]> splitRanges(int beg, int end, int depth) {
        if (end - beg <= 1) {
            boolean visible = !(rowDepth == maxDepth - 1 && depth > 0);
            return new ArrayList<>(List.of(new int[]{beg, end, visible ? 1 : 0}));
        }
        if (depth == 0) {
            return new ArrayList<>(List.of(new int[]{beg, end, 1}));
        }
        int m = (beg + end) / 2;
        var result = new ArrayList<>(splitRanges(beg, m, depth - 1));
        result.addAll(splitRanges(m, end, depth - 1));
        return result;
    }

    private void applyCommand(MergeSortCommand cmd) {
        if (cmd == null) return;
        var numbers = cmd.getNumbers().get(rowDepth);
        var visibilities = cmd.getVisibilities().get(rowDepth);
        var highlights = cmd.getHighlights().get(rowDepth);

        for (int i = 0; i < length; i++) {
            numberProperties[i].set(numbers.get(i));
            visibilityProperties[i].set(visibilities.get(i));
            highlightProperties[i].set(highlights.get(i));
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
