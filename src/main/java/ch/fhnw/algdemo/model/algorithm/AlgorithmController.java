package ch.fhnw.algdemo.model.algorithm;

import ch.fhnw.algdemo.model.command.Command;

import java.util.List;

public interface AlgorithmController {
    String getName();
    List<AlgorithmVariable<?>> getVariables();
    List<Command> getCommandHistory();
    List<String> getCommandSuggestions();
    void applyCommand(String commandExpression);
    void updateAlgorithmState(int selectedCommandId);
    int getHighestCommandId();
}
