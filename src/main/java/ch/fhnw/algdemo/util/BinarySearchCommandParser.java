package ch.fhnw.algdemo.util;

import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import ch.fhnw.algdemo.model.command.Command;

import java.util.List;

public class BinarySearchCommandParser extends CommandParser{
    private final List<Integer> data;

    public BinarySearchCommandParser(List<AlgorithmVariable<?>> variables, List<Integer> data) {
        super(variables);
        this.data = data;
    }

    public Command createCommand(String expression) throws IllegalArgumentException {
        var command = super.createCommand(expression);
        parseVariableAssignment(command);
        return command;
    }


    private void parseVariableAssignment(Command command) throws IllegalArgumentException {
        int value = Integer.parseInt(command.value);
        if (value < -1 || value > data.size()) {
            throw new IllegalArgumentException("Assignment seems illogical. Value " + value + " should be between -1 and " +  data.size());
        }

        if (command.variableName.equals("i")) {
            parseLowEndPointerAssignment(value);
        } else if (command.variableName.equals("j")) {
            parseHighEndPointerAssignment(value);
        } else {
            parseValuePointerAssignment(value);
        }
    }

    private void parseLowEndPointerAssignment(int commandValue) throws IllegalArgumentException {
        var i = getVariables().getFirst();
        var j = getVariables().get(1);

        if (j.value != null && commandValue > (Integer) j.value) {
            throw new IllegalArgumentException("Assignment seems illogical. Variable " + i.name + " should not be greater than Variable " +  j.name);
        }
    }

    private void parseHighEndPointerAssignment(int commandValue) {
        var i = getVariables().getFirst();
        var j = getVariables().get(1);

        if (i.value != null && commandValue < (Integer) i.value) {
            throw new IllegalArgumentException("Assignment seems illogical. Variable " + j.name + " should not be smaller than Variable " +  i.name);
        }
    }

    private void parseValuePointerAssignment(int commandValue) {
        var i = getVariables().getFirst();
        var j = getVariables().get(1);
        var m = getVariables().get(2);

        if (i.value != null && j.value != null && (commandValue < (Integer) i.value || commandValue > (Integer) j.value)) {
            throw new IllegalArgumentException("Assignment seems illogical. Variable " + m.name + " should be between Variables " +  i.name + " & " + j.name);
        }
    }
}
