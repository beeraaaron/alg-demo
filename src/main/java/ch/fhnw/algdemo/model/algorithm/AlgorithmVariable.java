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

    public T getTypedValue() {
        return value;
    }

    public void setValueFromObject(Object newValue) {
        if (newValue == null) {
            this.value = null;
        } else if (type.isInstance(newValue)) {
            this.value = type.cast(newValue);
        } else {
            throw new IllegalArgumentException(
                    "Cannot assign " + newValue.getClass() + " to variable of type " + type
            );
        }
    }
}
