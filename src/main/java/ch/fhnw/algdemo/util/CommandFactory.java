package ch.fhnw.algdemo.util;

import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import ch.fhnw.algdemo.model.algorithm.Command;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class CommandFactory {
    private static final Set<Character> validOperators = Set.of('(', ')', '+', '-', '/');

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
        var validVariableNames = variables.stream().map(v -> v.name).toList();
        if (variableName.isBlank()) {
            throw new IllegalArgumentException("Variable to be assigned must not be blank. Must be one of: " + validVariableNames);
        }
        var optionalVariable = variables.stream().filter(v -> v.name.equals(variableName)).findFirst();
        if (optionalVariable.isEmpty()) {
            throw new IllegalArgumentException("Invalid variable to be assigned '" + variableName + "'. Must be one of: " + validVariableNames);
        }

        var valueParts = parts[1].split(";");
        if (valueParts.length < 1) {
            throw new IllegalArgumentException("No value provided");
        }

        var expression = valueParts[0].trim();
        var variable = optionalVariable.get();
        if (expression.isBlank()) {
            throw new IllegalArgumentException("Value must not be blank");
        }

        if (isInt(expression)) {
            return new Command(command.trim(), variable.name + " = " + expression, variable.name, expression, nextCommandId.get(), true);
        } else {
            var parsedExpression = getParsedExpression(expression, variables);
            return new Command(parsedExpression, "TEST", false);
        }
    }

    public static String getParsedExpression(String expression, List<AlgorithmVariable<?>> variables) {
        Character lastChar = null;
        String parsedExpression = expression;
        var it = new StringCharacterIterator(expression);
        var validVariableNames = variables.stream().map(v -> v.name).toList();
        while (it.current() != CharacterIterator.DONE) {
            var ch = it.current();
            if (!Character.isWhitespace(ch) && !isAsciiAlphanumeric(ch) && !validOperators.contains(ch)) {
                throw new IllegalArgumentException("Invalid character '" + ch + "' in expression '" + expression + "'");
            }

            checkCharOccurrence(ch, lastChar, expression);

            if (validOperators.contains(ch)) {
                lastChar = ch;
                it.next();
            } else if (isAsciiLetter(ch)) {
                var sb = new StringBuilder();
                while (isAsciiLetter(it.current())) {
                    sb.append(it.current());
                    it.next();
                }
                var variableName = sb.toString();
                if (validVariableNames.contains(variableName)) {
                    var variable = variables.stream()
                            .filter(v -> v.name.equals(variableName))
                            .findFirst().orElseThrow();
                    if (variable.value != null) {
                        parsedExpression = parsedExpression.replaceFirst(variableName, variable.value.toString());
                        lastChar = variableName.charAt(variableName.length() - 1);
                    } else {
                        throw new IllegalArgumentException("Variable '" + variableName + "' is null and thus cannot be used in the expression");
                    }
                } else {
                    throw new IllegalArgumentException("Invalid variable '" + variableName + "' used in expression '" + expression + "'");
                }
            } else if (isAsciiNumber(ch)) {
                var sb = new StringBuilder();
                while (isAsciiNumber(it.current())) {
                    sb.append(it.current());
                    it.next();
                }
                var number = sb.toString();
                if (isInt(number)) {
                    lastChar = number.charAt(number.length() - 1);
                } else {
                    throw new IllegalArgumentException("Invalid number '" + number + "' in expression '" + expression + "'");
                }
            } else {
                it.next();
            }
        }

        int amountOfOpeningBraces = expression.length() - expression.replace("(", "").length();
        int amountOfClosingBraces = expression.length() - expression.replace(")", "").length();
        if (amountOfOpeningBraces != amountOfClosingBraces) {
            throw new IllegalArgumentException("Expression '" + expression + "' is invalid. There are different amounts of opening & closing brackets.");
        }

        //TODO check here that the expression has a valid ending.
        // operators at the end are not allowed: only when its )

        return parsedExpression;
    }

    private static void checkCharOccurrence(char currentChar, Character lastChar, String expression) {
        if (lastChar != null && !Character.isWhitespace(currentChar)) {
            if (isAsciiAlphanumeric(lastChar) && isAsciiAlphanumeric(currentChar)) {
                throw new IllegalArgumentException("Expression '" + expression + "' is invalid. On a Variable/Number there must always follow an operator.");
            } else if (isAsciiAlphanumeric(lastChar) && currentChar == '(') {
                throw new IllegalArgumentException("Expression '" + expression + "' is invalid. On a Variable/Number mustn't follow an opening Bracket '('");
            } else if (lastChar == ')' && isAsciiAlphanumeric(currentChar)) {
                throw new IllegalArgumentException("Expression '" + expression + "' is invalid. On a closing Bracket ')' mustn't follow a Variable/Number");
            } else if (validOperators.contains(lastChar) && validOperators.contains(currentChar)) {
                if (currentChar == '(') {
                    if (lastChar == ')') {
                        throw new IllegalArgumentException("Expression '" + expression + "' is invalid. On a closing Bracket ')' mustn't follow an opening Bracket '('");
                    }
                } else if (lastChar != ')') {
                    throw new IllegalArgumentException("Expression '" + expression + "' is invalid. Invalid use of operators.");
                }
            }
        }
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isAsciiAlphanumeric(char c) {
        return isAsciiLetter(c) || isAsciiNumber(c);
    }

    public static boolean isAsciiLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public static boolean isAsciiNumber(char c) {
        return c >= '0' && c <= '9';
    }
}
