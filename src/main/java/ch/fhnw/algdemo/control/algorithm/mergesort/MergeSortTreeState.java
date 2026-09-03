package ch.fhnw.algdemo.control.algorithm.mergesort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MergeSortTreeState {
    private final int depth;
    private final int[][] numbers;
    private final List<List<String>> indexes;
    private final boolean[][] visibilities;
    private final boolean[][] comparisons;
    private final boolean[][] overwrites;

    public MergeSortTreeState(int depth, List<Integer> a) {
        var length = a.size();
        this.depth = depth;
        numbers = new int[depth][length];
        indexes = new ArrayList<>();
        visibilities = new boolean[depth][length];
        comparisons = new boolean[depth][length];
        overwrites = new boolean[depth][length];

        for (int d = 0; d < depth; d++) {
            int rowSize = length + countRanges(0, length, d);
            List<String> row = new ArrayList<>(rowSize);
            for (int l = 0; l < rowSize; l++) {
                if (l < length) {
                    numbers[d][l] = a.get(l);
                }
                row.add("");
            }
            indexes.add(row);
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

    public void updateParentIndexes(int beg, int end, int m, int row, int length, boolean mergePhase) {
        if (mergePhase) {
            clearIndexes(row);
            clearIndexes(row + 1);
        } else if (row != 0) {
            clearIndexes(row - 1);
            clearIndexes(row);
        }

        int offset = computeOffset(length, beg, end, row);
        indexes.get(row).set(beg + offset, "beg");
        indexes.get(row).set(end + offset, "end");
        indexes.get(row).set(m + offset, "m");
    }

    public void initializeChildIndexes(int beg, int end, int j, int k, int row, int length) {
        int offset = computeOffset(length, beg, end, row);
        indexes.get(row).set(j + offset, "j");
        indexes.get(row).set(k + offset + 1, "k");
    }

    public void updateChildIndexes(int beg, int end, int index, String indexName, int row, int length) {
        int offset = computeOffset(length, beg, end, row);
        indexes.get(row).set(index - 1 + offset, "");
        indexes.get(row).set(index + offset, indexName);
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

    public void clearIndexes(int row) {
        Collections.fill(indexes.get(row), "");
    }

    public List<List<Integer>> getNumbers() {
        return deepCopy(numbers);

    }

    public List<List<String>> getIndexes() {
        return deepCopy(indexes);

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

    private int computeOffset(int length, int beg, int end, int row) {
        return computeOffset(0, length, row, beg, end);
    }

    private int computeOffset(int rangeBeg, int rangeEnd, int depth, int targetBeg, int targetEnd) {
        if (depth == 0 || rangeEnd - rangeBeg <= 1 || (rangeBeg == targetBeg && rangeEnd == targetEnd)) {
            return 0;
        }
        int m = (rangeBeg + rangeEnd) / 2;
        if (targetBeg < m) {
            return computeOffset(rangeBeg, m, depth - 1, targetBeg, targetEnd);
        }
        return countRanges(rangeBeg, m, depth - 1) + computeOffset(m, rangeEnd, depth - 1, targetBeg, targetEnd);
    }

    private int countRanges(int beg, int end, int depth) {
        if (depth == 0 || end - beg <= 1) return 1;
        int m = (beg + end) / 2;
        return countRanges(beg, m, depth - 1) + countRanges(m, end, depth - 1);
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

    private static <T> List<List<T>> deepCopy(List<List<T>> sourceArray) {
        var outputArray = new ArrayList<List<T>>(sourceArray.size());
        for (List<T> row : sourceArray) {
            var outputRow = new ArrayList<T>(row.size());
            outputRow.addAll(row);
            outputArray.add(outputRow);
        }
        return outputArray;
    }
}
