package ch.fhnw.algdemo.control.algorithm.mergesort;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class MergeSortRow extends GridPane {
    private final List<SimpleIntegerProperty> mergeSortArray;
    private final int rowDepth;
    private final int maxDepth;

    @FXML
    GridPane rootGridPane;

    public MergeSortRow(List<SimpleIntegerProperty> mergeSortArray, int rowDepth, int maxDepth) {
        this.mergeSortArray = mergeSortArray;
        this.rowDepth = rowDepth;
        this.maxDepth = maxDepth;
        loadFxController();
        initializeRow();
    }

    private void initializeRow() {
        List<int[]> ranges = splitRanges(0, mergeSortArray.size(), rowDepth);

        int columnAmount = mergeSortArray.size() * 3;
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
                    var col = new MergeSortColumn(mergeSortArray.get(j));
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

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("merge-sort-row.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
