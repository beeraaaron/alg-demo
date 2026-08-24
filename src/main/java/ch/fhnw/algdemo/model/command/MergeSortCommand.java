package ch.fhnw.algdemo.model.command;

import lombok.Getter;

import java.util.List;

public class MergeSortCommand extends Command {
    @Getter
    private final List<Integer> snapshot;

    public MergeSortCommand(String command, int id, List<Integer> snapshot) {
        super(command, id);
        this.snapshot = snapshot;
    }
}
