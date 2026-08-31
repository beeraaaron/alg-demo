package ch.fhnw.algdemo.model.command;

import lombok.Getter;

import java.util.List;

@Getter
public class MergeSortCommand extends Command {
    private final List<List<Integer>> numbers;
    private final List<List<Boolean>> visibilities;
    private final List<List<Boolean>> comparisons;
    private final List<List<Boolean>> overwrites;

    public MergeSortCommand(String command, int id,
                            List<List<Integer>> numbers,
                            List<List<Boolean>> visibilities,
                            List<List<Boolean>> comparisons,
                            List<List<Boolean>> overwrites) {
        super(command, id);
        this.numbers = numbers;
        this.visibilities = visibilities;
        this.comparisons = comparisons;
        this.overwrites = overwrites;
    }
}
