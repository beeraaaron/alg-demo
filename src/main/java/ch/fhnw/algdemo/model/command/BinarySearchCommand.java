package ch.fhnw.algdemo.model.command;

import lombok.Getter;
import lombok.Setter;

public class BinarySearchCommand extends Command{
    @Getter
    @Setter
    private String variableName;
    @Getter
    private final String result;
    @Getter
    @Setter
    private String value;
    @Getter
    private final boolean success;

    public BinarySearchCommand(String command, String result, String variableName, String value, boolean success) {
        super(command);
        this.result = result;
        this.variableName = variableName;
        this.value = value;
        this.success = success;
    }

    public BinarySearchCommand(String command, String result, boolean success) {
        super(command);
        this.result = result;
        this.success = success;
    }
}
