package ch.fhnw.algdemo.model.algorithm;

public class AlgorithmVariable<T> {
    public final String name;
    public final Class<T> type;
    public T value;

    public AlgorithmVariable(String name, Class<T> type, T value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public void setValueFromString(String stringValue) {
        this.value = parseValue(stringValue);
    }

    public boolean isValidValue(String stringValue) {
        if (stringValue == null) {
            return true;
        }

        try {
            parseValue(stringValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public T parseValue(String stringValue) {
        if (stringValue == null) {
            throw new IllegalArgumentException("Null is not a supported value");
        }

        if (type == Integer.class) {
            return (T) Integer.valueOf(stringValue);
        }

        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }

}
