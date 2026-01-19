package ch.fhnw.algdemo.model.algorithm;

public class AlgorithmVariable<T> {
    public String name;
    public final Class<T> type;
    public T value;

    public AlgorithmVariable(String name, Class<T> type, T value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public void setValueFromObject(Object newValue) {
        if (!type.isInstance(newValue)) {
            throw new IllegalArgumentException(
                    "Cannot assign " + newValue + " to variable of type " + type
            );
        }
        this.value = type.cast(newValue);
    }
}
