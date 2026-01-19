package ch.fhnw.algdemo.model.algorithm;

public class Command {
    public String command;
    public String result;
    public String variableName;
    public String value;
    public boolean success;

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
