package ch.fhnw.algdemo.model.algorithm;

import java.util.List;

public interface AlgorithmController {
    String getName();
    List<AlgorithmVariable<?>> getVariables();
    List<String> getCommandSuggestions();
    List<AlgorithmVariable<?>> applyCommand(String command);
}
