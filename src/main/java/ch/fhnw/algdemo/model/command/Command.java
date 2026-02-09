package ch.fhnw.algdemo.model.command;

public class Command {
    public final String command;
    public final String result;
    public final boolean success;
    public String variableName;
    public String value;
    public int id;

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
