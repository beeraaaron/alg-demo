package ch.fhnw.algdemo.test.util;

import ch.fhnw.algdemo.util.CommandParser;
import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import ch.fhnw.algdemo.model.algorithm.IntegerAlgorithmVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {
    private List<AlgorithmVariable<?>> variables;

    @BeforeEach
    void initialize() {
        this.variables = List.of(
                new IntegerAlgorithmVariable("i",  null),
                new IntegerAlgorithmVariable("j", null),
                new IntegerAlgorithmVariable("m", null)
        );
    }

    @Test
    void testCreateCommandSuccessfully() {
        var cp = new CommandParser(variables);
        var command = cp.createCommand("i = 0");
        assertEquals("i", command.getVariableName());
        assertEquals("0", command.getValue());
        assertEquals("i = 0", command.getCommand());
        assertEquals("i = 0", command.getResult());

        variables.getFirst().setValueFromString("0");

        command = cp.createCommand("j = 12");
        assertEquals("j", command.getVariableName());
        assertEquals("12", command.getValue());
        assertEquals("j = 12", command.getCommand());
        assertEquals("j = 12", command.getResult());

        variables.get(1).setValueFromString("12");

        command = cp.createCommand("m = (i+j) / 2");
        assertEquals("m", command.getVariableName());
        assertEquals("6", command.getValue());
        assertEquals("m = (i+j) / 2", command.getCommand());
        assertEquals("m = 6", command.getResult());

        variables.get(2).setValueFromString("6");

        command = cp.createCommand("i = m + 1");
        assertEquals("i", command.getVariableName());
        assertEquals("7", command.getValue());
        assertEquals("i = m + 1", command.getCommand());
        assertEquals("i = 7", command.getResult());

        variables.getFirst().setValueFromString("7");

        command = cp.createCommand("m = (i+j) / 2");
        assertEquals("m", command.getVariableName());
        assertEquals("9", command.getValue());
        assertEquals("m = (i+j) / 2", command.getCommand());
        assertEquals("m = 9", command.getResult());

        variables.get(2).setValueFromString("9");

        command = cp.createCommand("j = m");
        assertEquals("j", command.getVariableName());
        assertEquals("9", command.getValue());
        assertEquals("j = m", command.getCommand());
        assertEquals("j = 9", command.getResult());

        variables.get(1).setValueFromString("9");

        command = cp.createCommand("m = (i+j) / 2");
        assertEquals("m", command.getVariableName());
        assertEquals("8", command.getValue());
        assertEquals("m = (i+j) / 2", command.getCommand());
        assertEquals("m = 8", command.getResult());

        variables.get(2).setValueFromString("8");

        command = cp.createCommand("i = m + 1");
        assertEquals("i", command.getVariableName());
        assertEquals("9", command.getValue());
        assertEquals("i = m + 1", command.getCommand());
        assertEquals("i = 9", command.getResult());
    }

    @Test
    void testCreateCommandUnconventional() {
        variables.getFirst().setValueFromString("5");
        variables.get(1).setValueFromString("8");

        var cp = new CommandParser(variables);
        var command = cp.createCommand("m = (((i + 1) * 2) - j) / 4");
        assertEquals("m", command.getVariableName());
        assertEquals("1", command.getValue());
        assertEquals("m = (((i + 1) * 2) - j) / 4", command.getCommand());
        assertEquals("m = 1", command.getResult());

        command = cp.createCommand("m = ((i + 1) / (j / 2)) * 4");
        assertEquals("m", command.getVariableName());
        assertEquals("4", command.getValue());
        assertEquals("m = ((i + 1) / (j / 2)) * 4" , command.getCommand());
        assertEquals("m = 4", command.getResult());

        command = cp.createCommand("m = i + j / 2 - 3 * 4");
        assertEquals("m", command.getVariableName());
        assertEquals("-3", command.getValue());
        assertEquals("m = i + j / 2 - 3 * 4" , command.getCommand());
        assertEquals("m = -3", command.getResult());

        command = cp.createCommand("m = (i + 5 / 2)");
        assertEquals("m", command.getVariableName());
        assertEquals("7", command.getValue());
        assertEquals("m = (i + 5 / 2)" , command.getCommand());
        assertEquals("m = 7", command.getResult());
    }

    @Test
    void testCreateCommand_assigningInvalidVariable() {
        var cp = new CommandParser(variables);

        var exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("1 = 0"));
        assertTrue(exception.getMessage().contains("Invalid variable to be assigned. Must be one of:"));

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("$ = 100"));
        assertTrue(exception.getMessage().contains("Invalid variable to be assigned. Must be one of:"));

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand(""));
        assertTrue(exception.getMessage().contains("Invalid variable to be assigned. Must be one of:"));
    }

    @Test
    void testCreateCommand_assigningUnknownVariable() {
        var cp = new CommandParser(variables);

        var exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("test1 = 1"));
        assertTrue(exception.getMessage().contains("Invalid variable used 'test'. Must be one of:"));

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("I = 1"));
        assertTrue(exception.getMessage().contains("Invalid variable used 'I'. Must be one of:"));

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("x = 5"));
        assertTrue(exception.getMessage().contains("Invalid variable used 'x'. Must be one of:"));
    }

    @Test
    void testCreateCommand_expectingEquals() {
        var cp = new CommandParser(variables);

        var exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("i 100"));
        assertTrue(exception.getMessage().contains("Command must contain '='"));

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("j+"));
        assertTrue(exception.getMessage().contains("Command must contain '='"));

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("m 7"));
        assertTrue(exception.getMessage().contains("Command must contain '='"));
    }

    @Test
    void testCreateCommand_divisionByZero() {
        var cp = new CommandParser(variables);

        var exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("i = 20 / 0"));
        assertTrue(exception.getMessage().contains("Expression is invalid. Division by zero is not allowed."));

        variables.getFirst().setValueFromString("5");
        variables.get(1).setValueFromString("10");

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("m = ((i + j) / 2) / 0"));
        assertTrue(exception.getMessage().contains("Expression is invalid. Division by zero is not allowed."));
    }

    @Test
    void testCreateCommand_expectingClosingBracket() {
        var cp = new CommandParser(variables);

        var exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("i = (10 / 2"));
        assertTrue(exception.getMessage().contains("Expression is invalid. Expected ')' to close parenthesis."));

        variables.getFirst().setValueFromString("5");
        variables.get(1).setValueFromString("10");

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("m = ((i + j / 2)"));
        assertTrue(exception.getMessage().contains("Expression is invalid. Expected ')' to close parenthesis."));
    }

    @Test
    void testCreateCommand_nullVariable() {
        var cp = new CommandParser(variables);
        variables.get(1).setValueFromString("10");

        var exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("m = (i + j) / 2"));
        assertTrue(exception.getMessage().contains("Variable 'i' is null and thus cannot be used in the expression"));

        variables.getFirst().setValueFromString("5");
        variables.get(1).setValueFromString("10");

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("i = m + 1"));
        assertTrue(exception.getMessage().contains("Variable 'm' is null and thus cannot be used in the expression"));
    }
}
