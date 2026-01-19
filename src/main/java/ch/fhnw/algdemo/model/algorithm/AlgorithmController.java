package ch.fhnw.algdemo.model.algorithm;

import java.util.List;

public interface AlgorithmController {
    String getName();
    List<AlgorithmVariable<?>> getVariables();
    List<Command> getCommandHistory();
    List<String> getCommandSuggestions();
    void applyCommand(String command);
}
