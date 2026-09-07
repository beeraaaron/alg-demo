package ch.fhnw.algdemo.model.algorithm;

import lombok.Getter;
import lombok.Setter;

public abstract class AlgorithmVariable<T> {
    @Getter
    private final String name;
    @Getter
    @Setter
    private T value;

    public AlgorithmVariable(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public abstract void setValueFromString(String stringValue);
}
