package ch.fhnw.algdemo.model.algorithm;

public class Command {
    public final String command;
    public final String result;
    public final boolean success;
    public String variableName;
    public String value;
    public int id;

    public Command(String command, String result, String variableName, String value, int id, boolean success) {
        this.command = command;
        this.result = result;
        this.variableName = variableName;
        this.value = value;
        this.id = id;
        this.success = success;
    }

    public Command(String command, String result, boolean success) {
        this.command = command;
        this.result = result;
        this.success = success;
    }
}
