package ch.fhnw.algdemo.util;

import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import ch.fhnw.algdemo.model.algorithm.Command;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class CommandFactory {
    private final Set<Character> validOperators = Set.of('(', ')', '+', '-', '/');

    public static Command createCommand(String command, List<AlgorithmVariable<?>> variables,
                                        Supplier<Integer> nextCommandId) throws IllegalArgumentException {
        if (!command.contains("=")) {
            throw new IllegalArgumentException("Command must contain '='");
        }
        if (!command.contains(";")) {
            throw new IllegalArgumentException("Command must contain ';' at the end");
        }

        var parts = command.split("=");
        var variableName = parts[0].trim();
        var optionalVariable = variables.stream().filter(v -> v.name.equals(variableName)).findFirst();
        if (variableName.isBlank() || optionalVariable.isEmpty()) {
            var variableNames = variables.stream().map(v -> v.name).toList();
            throw new IllegalArgumentException("Invalid variable '" + variableName + "'. Must be one of: " + variableNames);
        }

        var valueParts = parts[1].split(";");
        if (valueParts.length < 1) {
            throw new IllegalArgumentException("No value provided");
        }
        var value = valueParts[0].trim();
        var variable = optionalVariable.get();

        //TODO: if value is a number validate correctness
        // if it's not a number, try to evaluate condition and then check correctness

        if (value.isBlank()) {
            throw new IllegalArgumentException("Value must not be blank");
        } else if (!isInt(value)) {
            throw new IllegalArgumentException("Invalid value '" + value + "'. Must be of same type as variable "
                    + variable.name);
        }


        return new Command(command.trim(), variable.name + " = " + value, variable.name,
                value, nextCommandId.get(), true);
    }

//    public void x(String value, List<AlgorithmVariable<?>> variables) {
//        // TODO search for variables and then replace them with its value
//
//        // TODO search for
//        for (int i  = 0; i < value.length(); i++) {
//            Character ch = value.charAt(i);
//            if (!Character.isWhitespace(ch) && (!validOperators.contains(ch)
//                    || !variables.stream().anyMatch(v -> v.name.equals(ch))) {
//                throw new IllegalArgumentException("Invalid character '" + ch + "'");
//            }
//            if () {}
//
//        }
//    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
