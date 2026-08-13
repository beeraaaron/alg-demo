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
        int value = Integer.parseInt(command.getValue());
        if (value < -1 || value > data.size()) {
            throw new IllegalArgumentException("Assignment seems illogical. Value " + value + " should be between -1 and " +  data.size());
        }

        if (command.getVariableName().equals("i")) {
            parseLowEndPointerAssignment(value);
        } else if (command.getVariableName().equals("j")) {
            parseHighEndPointerAssignment(value);
        } else {
            parseValuePointerAssignment(value);
        }
    }

    private void parseLowEndPointerAssignment(int commandValue) throws IllegalArgumentException {
        var i = getVariables().getFirst();
        var j = getVariables().get(1);

        if (j.getValue() != null && commandValue > (Integer) j.getValue()) {
            throw new IllegalArgumentException("Assignment seems illogical. Variable " + i.getName() + " should not be greater than Variable " +  j.getName());
        }
    }

    private void parseHighEndPointerAssignment(int commandValue) {
        var i = getVariables().getFirst();
        var j = getVariables().get(1);

        if (i.getValue() != null && commandValue < (Integer) i.getValue()) {
            throw new IllegalArgumentException("Assignment seems illogical. Variable " + j.getName() + " should not be smaller than Variable " +  i.getName());
        }
    }

    private void parseValuePointerAssignment(int commandValue) {
        var i = getVariables().getFirst();
        var j = getVariables().get(1);
        var m = getVariables().get(2);

        if (i.getValue() != null && j.getValue() != null && (commandValue < (Integer) i.getValue() || commandValue > (Integer) j.getValue())) {
            throw new IllegalArgumentException("Assignment seems illogical. Variable " + m.getName() + " should be between Variables " +  i.getName() + " & " + j.getName());
        }
    }
}
