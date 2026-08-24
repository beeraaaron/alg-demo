package ch.fhnw.algdemo.control.algorithm.mergesort;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class MergeSortRow extends GridPane {
    private final List<Integer> mergeSortArray;
    private final int rowDepth;

    @FXML
    GridPane rootGridPane;

    public MergeSortRow(List<Integer> mergeSortArray, int i) {
        this.mergeSortArray = mergeSortArray;
        this.rowDepth = i;
        loadFxController();
        initializeRow();
    }

    private void initializeRow() {
        List<int[]> ranges = splitRanges(0, mergeSortArray.size(), rowDepth);

        var columnAmount = mergeSortArray.size() + ranges.size() * 2;
        var percentWidth = 100.0 / columnAmount;
        for (int i = 0; i < columnAmount; i++) {
            var cc = new ColumnConstraints();
            cc.setPercentWidth(percentWidth);
            rootGridPane.getColumnConstraints().add(cc);
        }

        int column = 1;
        for (int[] range : ranges) {
            for (int j = range[0]; j < range[1]; j++) {
                var col = new MergeSortColumn(mergeSortArray.get(j), j);
                rootGridPane.add(col, column++, 0);
            }
            column += 2;
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

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("merge-sort-row.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }
}
