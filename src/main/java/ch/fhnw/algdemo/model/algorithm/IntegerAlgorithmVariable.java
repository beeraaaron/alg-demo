package ch.fhnw.algdemo.model.algorithm;

public class IntegerAlgorithmVariable extends AlgorithmVariable<Integer> {

    public IntegerAlgorithmVariable(String name, Integer value) {
        super(name, value);
    }

    @Override
    public void setValueFromString(String stringValue) {
        if (stringValue == null) {
            throw new IllegalArgumentException("Null is not a supported value");
        }

        try {
            super.setValue(Integer.valueOf(stringValue));
        } catch (Exception e) {
            throw new IllegalArgumentException("Unsupported value: " + stringValue);
        }
    }
}
