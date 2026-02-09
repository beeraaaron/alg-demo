package ch.fhnw.algdemo.util;

import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import ch.fhnw.algdemo.model.command.Command;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CommandParser {
    @Getter
    private final List<AlgorithmVariable<?>> variables;
    private final List<String> variableNames;
    @Setter
    @Getter
    private String input;
    @Setter
    @Getter
    private int pos;

    public CommandParser(List<AlgorithmVariable<?>> variables) {
        this.variables = variables;
        this.variableNames = variables.stream().map(v -> v.name).toList();
    }

    public Command createCommand(String expression) throws IllegalArgumentException {
        setInput(expression.replaceAll("\\s+", ""));
        setPos(0);
        var variable = parseVariable();

        if (!(peek() == '=')) {
            throw new IllegalArgumentException("Command must contain '='");
        }
        consume();

        var value = parseExpression();

        if (getPos() < getInput().length()) {
            throw new IllegalArgumentException("Unexpected characters in expression. Allowed are whitespaces, variables, " +
                    "whole numbers, '(', ')' and operators: '+', '-', '/', '*'");
        }

        return new Command(expression, variable.name + " = " + value, variable.name, String.valueOf(value), true);
    }

    public AlgorithmVariable<?> parseVariable() throws IllegalArgumentException {
        if (pos >= input.length() || !isAsciiLetter(peek())) {
            throw new IllegalArgumentException("Invalid variable to be assigned. Must be one of: " + variableNames);
        }

        var sb = new StringBuilder();
        while (pos < input.length() && isAsciiLetter(peek())) {
            sb.append(consume());
        }
        var variableName = sb.toString();

        int idx = variableNames.indexOf(variableName);
        if  (idx == -1) {
            throw new IllegalArgumentException("Invalid variable used '" + variableName + "'. Must be one of: " + variableNames);
        }

        return variables.get(idx);
    }

    public int parseExpression() throws IllegalArgumentException {
        int result = parseTerm();

        while (pos < input.length() && (peek() == '+' || peek() == '-')) {
            char op = consume();
            int right = parseTerm();

            if (op == '+') {
                result = result + right;
            } else {
                result = result - right;
            }
        }
        return result;
    }

    public int parseTerm() throws IllegalArgumentException {
        int result = parseFactor();

        while (pos < input.length() && (peek() == '*' || peek() == '/')) {
            char op = consume();
            int right = parseFactor();

            if (op == '*') {
                result = result * right;
            } else {
                if (right == 0) {
                    throw new IllegalArgumentException("Expression is invalid. Division by zero is not allowed.");
                }
                result = result / right;
            }
        }
        return result;
    }

    public int parseFactor() throws IllegalArgumentException {
        if (peek() == '(') {
            consume();
            int result = parseExpression();

            if (peek() != ')') {
                throw new IllegalArgumentException("Expression is invalid. Expected ')' to close parenthesis.");
            }
            consume();

            return result;
        } else if (isAsciiNumber(peek())) {
            return parseNumber();
        } else if (isAsciiLetter(peek())) {
            return parseVariableValue();
        }
        throw new IllegalArgumentException("Expression is invalid. Expected number, variable, or '(' at position " + pos);
    }

    public int parseVariableValue() throws IllegalArgumentException {
        var variable = parseVariable();

        if (variable.value == null) {
            throw new IllegalArgumentException("Variable '" + variable.name + "' is null and thus cannot be used in the expression");
        }

        return (Integer) variable.value;
    }

    public int parseNumber() throws IllegalArgumentException {
        int start = pos;

        while (pos < input.length() && isAsciiNumber(peek())) {
            pos++;
        }

        if (start == pos) {
            throw new IllegalArgumentException("Expression is invalid. Expected number, variable, or '(' at position " + pos);
        }

        return Integer.parseInt(input.substring(start, pos));
    }

    private boolean isAsciiLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isAsciiNumber(char c) {
        return (c >= '0' && c <= '9') || c == '-' || c == '+';
    }

    public char peek() {
        return pos < input.length() ? input.charAt(pos) : '\0';
    }

    public char consume() {
        return input.charAt(pos++);
    }
}
