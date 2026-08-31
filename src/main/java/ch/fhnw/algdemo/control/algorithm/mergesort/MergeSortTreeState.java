package ch.fhnw.algdemo.control.algorithm.mergesort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSortTreeState {
    private final int depth;
    private final int[][] numbers;
    private final boolean[][] visibilities;
    private final boolean[][] comparisons;
    private final boolean[][] overwrites;

    public MergeSortTreeState(int depth, List<Integer> a) {
        var length = a.size();
        this.depth = depth;
        numbers = new int[depth][length];
        visibilities = new boolean[depth][length];
        comparisons = new boolean[depth][length];
        overwrites = new boolean[depth][length];

        for (int d = 0; d < depth; d++) {
            for (int l = 0; l < length; l++) {
                numbers[d][l] = a.get(l);
            }
        }
        Arrays.fill(visibilities[0], true);
    }

    public void touch(int row, int beg, int end) {
        for (int i = beg; i < end; i++) {
            visibilities[row][i] = true;
        }
    }

    public void compare(int j, int k, int childRow) {
        clearComparisons(childRow);
        comparisons[childRow][j] = true;
        comparisons[childRow][k] = true;
    }

    public void editTempArray(Integer number, int lower, int upper, int boundary, int i, int row, int childRow) {
        if (upper == boundary) {
            clearComparisons(childRow);
            comparisons[childRow][lower] = true;
            comparisons[childRow][upper - 1] = false;
        }
        overwrites[row][i] = true;
        numbers[row][i] = number;
    }

    public void editArray(Integer number, int y) {
        numbers[0][y] = number;
        overwrites[0][y] = true;
    }

    public void completeMerge(int row, int beg, int end) {
        for (int i = beg; i < end; i++) {
            visibilities[row][i] = true;
            comparisons[row][i] = false;

            if (row + 1 < depth) {
                visibilities[row + 1][i] = false;
                comparisons[row + 1][i] = false;
            }
        }
    }

    public void hideRow(int row, int beg, int end) {
        for (int y = beg; y < end; y++) {
            visibilities[row][y] = false;
        }
        clearComparisons(row);
    }

    public void clearComparisons(int row) {
        Arrays.fill(comparisons[row], false);
    }

    public void clearOverwrites(int row) {
        Arrays.fill(overwrites[row], false);
    }

    public List<List<Integer>> getNumbers() {
        return deepCopy(numbers);

    }

    public List<List<Boolean>> getVisibilities() {
        return deepCopy(visibilities);
    }

    public List<List<Boolean>> getComparisons() {
        return deepCopy(comparisons);
    }

    public List<List<Boolean>> getOverwrites() {
        return deepCopy(overwrites);
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
