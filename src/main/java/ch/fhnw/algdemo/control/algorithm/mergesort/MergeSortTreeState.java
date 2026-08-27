package ch.fhnw.algdemo.control.algorithm.mergesort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSortTreeState {
    final int depth;
    final int length;
    final int[][] numbers;
    final boolean[][] visible;
    final boolean[][] highlight;

    public MergeSortTreeState(int depth, List<Integer> a) {
        this.depth = depth;
        length = a.size();
        numbers = new int[depth][length];
        visible = new boolean[depth][length];
        highlight = new boolean[depth][length];

        for (int d = 0; d < depth; d++) {
            for (int l = 0; l < length; l++) {
                numbers[d][l] = a.get(l);
            }
        }
        Arrays.fill(visible[0], true);
    }

    public void touch(int row, int beg, int end) {
        for (int i = beg; i < end; i++) {
            //numbers[row][i] = a.get(i);
            visible[row][i] = true;
        }
    }

    public void clearHighlights(int row) {
        Arrays.fill(highlight[row], false);
    }

    public void completeMerge(int row, int beg, int end, List<Integer> a) {
        for (int i = beg; i < end; i++) {
            numbers[row][i] = a.get(i);
            visible[row][i] = true;
            highlight[row][i] = false;

            numbers[0][i] = a.get(i);

            if (row + 1 < depth) {
                visible[row + 1][i] = false;
                highlight[row + 1][i] = false;
            }
        }
    }

    public List<List<Integer>> getNumbers() {
        return deepCopy(numbers);

    }

    public List<List<Boolean>> getVisibilities() {
        return deepCopy(visible);
    }

    public List<List<Boolean>> getHighlights() {
        return deepCopy(highlight);
    }

    private static List<List<Integer>> deepCopy(int[][] sourceArray) {
        var outputArray = new ArrayList<List<Integer>>(sourceArray.length);
        for (int[] row : sourceArray) {
            var outputRow = new ArrayList<Integer>(row.length);
            for (int i : row) {
                outputRow.add(i);
            }
            outputArray.add(outputRow);
        }
        return outputArray;
    }

    private static List<List<Boolean>> deepCopy(boolean[][] sourceArray) {
        var outputArray = new ArrayList<List<Boolean>>(sourceArray.length);
        for (boolean[] row : sourceArray) {
            var outputRow = new ArrayList<Boolean>(row.length);
            for (boolean b : row) {
                outputRow.add(b);
            }
            outputArray.add(outputRow);
        }
        return outputArray;
    }
}
