package ch.fhnw.algdemo.model.algorithm;

public abstract class AlgorithmVariable<T> {
    public final String name;
    public T value;

    public AlgorithmVariable(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public abstract void setValueFromString(String stringValue);

    public abstract T parseValue(String stringValue);
}
