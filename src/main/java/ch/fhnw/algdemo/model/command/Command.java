package ch.fhnw.algdemo.model.command;

import lombok.Getter;
import lombok.Setter;

public class Command {
    @Getter
    private final String command;
    @Getter
    private final String result;
    @Getter
    private final boolean success;
    @Getter
    @Setter
    private String variableName;
    @Getter
    @Setter
    private String value;
    @Getter
    @Setter
    private int id;

    public Command(String command, String result, String variableName, String value, boolean success) {
        this.command = command;
        this.result = result;
        this.variableName = variableName;
        this.value = value;
        this.success = success;
    }

    public Command(String command, String result, boolean success) {
        this.command = command;
        this.result = result;
        this.success = success;
    }
}
