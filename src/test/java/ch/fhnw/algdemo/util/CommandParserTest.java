package ch.fhnw.algdemo.util;

import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import ch.fhnw.algdemo.model.algorithm.IntegerAlgorithmVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {
    private List<AlgorithmVariable<?>> variables;
    private Integer commandId = 0;

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
        var command = cp.createCommand("i = 0", this::getNextCommandId);
        assertEquals("i", command.variableName);
        assertEquals("0", command.value);
        assertEquals("i = 0", command.command);
        assertEquals("i = 0", command.result);

        variables.getFirst().setValueFromString("0");

        command = cp.createCommand("j = 12", this::getNextCommandId);
        assertEquals("j", command.variableName);
        assertEquals("12", command.value);
        assertEquals("j = 12", command.command);
        assertEquals("j = 12", command.result);

        variables.get(1).setValueFromString("12");

        command = cp.createCommand("m = (i+j) / 2", this::getNextCommandId);
        assertEquals("m", command.variableName);
        assertEquals("6", command.value);
        assertEquals("m = (i+j) / 2", command.command);
        assertEquals("m = 6", command.result);

        variables.get(2).setValueFromString("6");

        command = cp.createCommand("i = m + 1", this::getNextCommandId);
        assertEquals("i", command.variableName);
        assertEquals("7", command.value);
        assertEquals("i = m + 1", command.command);
        assertEquals("i = 7", command.result);

        variables.getFirst().setValueFromString("7");

        command = cp.createCommand("m = (i+j) / 2", this::getNextCommandId);
        assertEquals("m", command.variableName);
        assertEquals("9", command.value);
        assertEquals("m = (i+j) / 2", command.command);
        assertEquals("m = 9", command.result);

        variables.get(2).setValueFromString("9");

        command = cp.createCommand("j = m", this::getNextCommandId);
        assertEquals("j", command.variableName);
        assertEquals("9", command.value);
        assertEquals("j = m", command.command);
        assertEquals("j = 9", command.result);

        variables.get(1).setValueFromString("9");

        command = cp.createCommand("m = (i+j) / 2", this::getNextCommandId);
        assertEquals("m", command.variableName);
        assertEquals("8", command.value);
        assertEquals("m = (i+j) / 2", command.command);
        assertEquals("m = 8", command.result);

        variables.get(2).setValueFromString("8");

        command = cp.createCommand("i = m + 1", this::getNextCommandId);
        assertEquals("i", command.variableName);
        assertEquals("9", command.value);
        assertEquals("i = m + 1", command.command);
        assertEquals("i = 9", command.result);
    }

    @Test
    void testCreateCommandUnconventional() {
        variables.getFirst().setValueFromString("5");
        variables.get(1).setValueFromString("8");

        var cp = new CommandParser(variables);
        var command = cp.createCommand("m = (((i + 1) * 2) - j) / 4", this::getNextCommandId);
        assertEquals("m", command.variableName);
        assertEquals("1", command.value);
        assertEquals("m = (((i + 1) * 2) - j) / 4", command.command);
        assertEquals("m = 1", command.result);

        command = cp.createCommand("m = ((i + 1) / (j / 2)) * 4", this::getNextCommandId);
        assertEquals("m", command.variableName);
        assertEquals("4", command.value);
        assertEquals("m = ((i + 1) / (j / 2)) * 4" , command.command);
        assertEquals("m = 4", command.result);

        command = cp.createCommand("m = i + j / 2 - 3 * 4", this::getNextCommandId);
        assertEquals("m", command.variableName);
        assertEquals("-3", command.value);
        assertEquals("m = i + j / 2 - 3 * 4" , command.command);
        assertEquals("m = -3", command.result);

        command = cp.createCommand("m = (i + 5 / 2)", this::getNextCommandId);
        assertEquals("m", command.variableName);
        assertEquals("7", command.value);
        assertEquals("m = (i + 5 / 2)" , command.command);
        assertEquals("m = 7", command.result);
    }

    @Test
    void testCreateCommand_assigningInvalidVariable() {
        var cp = new CommandParser(variables);

        var exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("1 = 0", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Invalid variable to be assigned. Must be one of:"));

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("$ = 100", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Invalid variable to be assigned. Must be one of:"));

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Invalid variable to be assigned. Must be one of:"));
    }

    @Test
    void testCreateCommand_assigningUnknownVariable() {
        var cp = new CommandParser(variables);

        var exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("test1 = 1", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Invalid variable used 'test'. Must be one of:"));

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("I = 1", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Invalid variable used 'I'. Must be one of:"));

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("x = 5", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Invalid variable used 'x'. Must be one of:"));
    }

    @Test
    void testCreateCommand_expectingEquals() {
        var cp = new CommandParser(variables);

        var exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("i 100", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Command must contain '='"));

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("j+", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Command must contain '='"));

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("m 7", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Command must contain '='"));
    }

    @Test
    void testCreateCommand_divisionByZero() {
        var cp = new CommandParser(variables);

        var exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("i = 20 / 0", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Expression is invalid. Division by zero is not allowed."));

        variables.getFirst().setValueFromString("5");
        variables.get(1).setValueFromString("10");

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("m = ((i + j) / 2) / 0", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Expression is invalid. Division by zero is not allowed."));
    }

    @Test
    void testCreateCommand_expectingClosingBracket() {
        var cp = new CommandParser(variables);

        var exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("i = (10 / 2", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Expression is invalid. Expected ')' to close parenthesis."));

        variables.getFirst().setValueFromString("5");
        variables.get(1).setValueFromString("10");

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("m = ((i + j / 2)", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Expression is invalid. Expected ')' to close parenthesis."));
    }

    @Test
    void testCreateCommand_nullVariable() {
        var cp = new CommandParser(variables);
        variables.get(1).setValueFromString("10");

        var exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("m = (i + j) / 2", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Variable 'i' is null and thus cannot be used in the expression"));

        variables.getFirst().setValueFromString("5");
        variables.get(1).setValueFromString("10");

        exception = assertThrows(IllegalArgumentException.class, () -> cp.createCommand("i = m + 1", this::getNextCommandId));
        assertTrue(exception.getMessage().contains("Variable 'm' is null and thus cannot be used in the expression"));
    }

    Integer getNextCommandId() {
        return commandId++;
    }
}
