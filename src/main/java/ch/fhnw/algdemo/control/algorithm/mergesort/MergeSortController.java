package ch.fhnw.algdemo.control.algorithm.mergesort;

import ch.fhnw.algdemo.control.MainController;
import ch.fhnw.algdemo.model.algorithm.AlgorithmController;
import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import ch.fhnw.algdemo.model.command.Command;
import ch.fhnw.algdemo.model.algorithm.IntegerAlgorithmVariable;
import ch.fhnw.algdemo.model.command.MergeSortCommand;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class MergeSortController extends GridPane implements AlgorithmController {
    private final List<Command> commandHistory = new ArrayList<>();
    private final List<MergeSortCommand> fullCommandHistory = new ArrayList<>();
    private int highestCommandId = 0;
    private int selectedCommandId = 0;

    private final List<AlgorithmVariable<?>> variables = List.of(
            new IntegerAlgorithmVariable("someName", null)
    );

    private final List<String> arraySizeOptions = List.of("4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16");
    private final List<Integer> initialMergeSortArray = new ArrayList<>(List.of(16, 15, 14, 13, 12 ,11 ,10 ,9, 8, 7, 6, 5, 4, 3, 2, 1));
    private List<Integer> updatedMergeSortArray = new ArrayList<>(initialMergeSortArray);
    private int selectedArraySize = 16;

    private final MainController mainController;

    @FXML
    GridPane mergeSortGridPane;
    @FXML
    ChoiceBox<String> arraySizeChoiceBox;
    @FXML
    Button startButton;
    @FXML
    Button stopButton;
    @FXML
    Button prevButton;
    @FXML
    Button nextButton;

    public MergeSortController(MainController mainController) {
        this.mainController = mainController;
        loadFxController();
        arraySizeChoiceBox.getSelectionModel().select(4);
    }

    @FXML
    public void initialize() {
        startButton.setOnMouseClicked(e -> onStartClicked());
        stopButton.setOnMouseClicked(e -> onStopClicked());
        prevButton.setOnMouseClicked(e -> onPrevClicked());
        nextButton.setOnMouseClicked(e -> onNextClicked());
        configureArraySizeChoiceBox();
    }

    @Override
    public void updateAlgorithmState(int selectedCommandId) {
        this.selectedCommandId = selectedCommandId;
        if (selectedCommandId == 0) {
            updatedMergeSortArray = new ArrayList<>(initialMergeSortArray.subList(0, selectedArraySize));
        } else {
            var found = commandHistory.stream().filter(c -> c.getId() == selectedCommandId).findFirst();
            if (found.isPresent() && found.get() instanceof MergeSortCommand msc) {
                updatedMergeSortArray = new ArrayList<>(msc.getSnapshot());
            }
        }
        initializeMergeSort();
    }

    private void generateHistory() {
        fullCommandHistory.clear();
        commandHistory.clear();

        var a = new ArrayList<>(initialMergeSortArray.subList(0, selectedArraySize));
        sort(a, 0, selectedArraySize);

        highestCommandId = 0;
        selectedCommandId = 0;
    }

    private void sort(List<Integer> a, int beg, int end) {
        if (end - beg > 1) {
            int m = (beg + end) / 2;
            recordSnapshot(a, "sort(a, " + beg + ", " + end + ")");
            sort(a, beg, m);
            sort(a, m, end);
            merge(a, beg, m, end);
        }
    }

    private void merge(List<Integer> a, int beg, int m, int end) {
        int i = 0, j = beg, k = m;
        var b = new int[end - beg];

        while (i < end - beg) {
            if (k == end || (j < m && compare(a, j, k))) {
                b[i] = a.get(j);
                recordSnapshot(a, "b[" + i + "] = a[" + j + "]");
                j++;
            } else {
                b[i] = a.get(k);
                recordSnapshot(a, "b[" + i + "] = a[" + k + "]");
                k++;
            }
            i++;
        }

        i = 0;
        for (int y = beg; y < end; y++) {
            a.set(y, b[i]);
            recordSnapshot(a, "a[" + y + "] = b[" + i + "]");
            i++;
        }
    }

    private boolean compare(List<Integer> a, int j, int k) {
        recordSnapshot(a, "a[" + j + "] < a[" + k + "]");
        return a.get(j) < a.get(k);
    }

    private void recordSnapshot(List<Integer> a, String description) {
        var cmd = new MergeSortCommand(description, selectedCommandId + 1, new ArrayList<>(a));
        fullCommandHistory.add(cmd);
        selectedCommandId++;
    }

    private void onStartClicked() {
        startButton.setVisible(false);
        startButton.setManaged(false);
        prevButton.setVisible(true);
        prevButton.setManaged(true);
        nextButton.setVisible(true);
        nextButton.setManaged(true);
        onNextClicked();
    }

    private void onStopClicked() {
        fullCommandHistory.clear();
        commandHistory.clear();
        highestCommandId = 0;
        selectedCommandId = 0;
        mainController.applyCommand();

        updatedMergeSortArray = new ArrayList<>(initialMergeSortArray.subList(0, selectedArraySize));
        initializeMergeSort();

        prevButton.setVisible(false);
        prevButton.setManaged(false);
        nextButton.setVisible(false);
        nextButton.setManaged(false);
        startButton.setVisible(true);
        startButton.setManaged(true);
    }

    private void onPrevClicked() {
        if (selectedCommandId > 0) {
            selectedCommandId--;
            updateAlgorithmState(selectedCommandId);

            if (selectedCommandId > 0) {
                prevButton.setVisible(false);
                prevButton.setManaged(false);
                nextButton.setVisible(true);
                nextButton.setManaged(true);
            }
        }
    }

    private void onNextClicked() {
        if (selectedCommandId == highestCommandId && selectedCommandId < fullCommandHistory.size()) {
            var c = fullCommandHistory.get(selectedCommandId);
            commandHistory.add(c);
            highestCommandId = c.getId();
            selectedCommandId = c.getId();
            updateAlgorithmState(selectedCommandId);
            mainController.applyCommand();

            if (selectedCommandId >= fullCommandHistory.size()) {
                prevButton.setVisible(true);
                prevButton.setManaged(true);
                nextButton.setVisible(false);
                nextButton.setManaged(false);
            }
        } else if (selectedCommandId < highestCommandId) {
            selectedCommandId = commandHistory.get(selectedCommandId).getId();
            updateAlgorithmState(selectedCommandId);
            mainController.applyCommand();

            prevButton.setVisible(true);
            prevButton.setManaged(true);
            nextButton.setVisible(true);
            nextButton.setManaged(true);
        }
    }

    private void initializeMergeSort() {
        mergeSortGridPane.getChildren().clear();
        mergeSortGridPane.getRowConstraints().clear();
        var mergeSortDepth = calculateMergeSortDepth();

        for (int i = 0; i < mergeSortDepth; i++) {
            var rc = new RowConstraints();
            rc.setPercentHeight((double)100/mergeSortDepth);
            mergeSortGridPane.getRowConstraints().add(rc);
            var row = new MergeSortRow(updatedMergeSortArray, i);
            mergeSortGridPane.add(row, 0, i);
        }
    }

    private void changeArraySize(String newValue) {
        var newArraySize = Integer.parseInt(newValue);
        changeMergeSortArray(newArraySize, selectedArraySize);
        selectedArraySize = newArraySize;
        updatedMergeSortArray = new ArrayList<>(initialMergeSortArray.subList(0, selectedArraySize));

        generateHistory();
        selectedCommandId = 0;
        initializeMergeSort();
    }

    private void changeMergeSortArray(int newArraySize, int oldArraySize) {
        if (oldArraySize != newArraySize) {
            if (oldArraySize < newArraySize) {
                for (int i = 1; i <= newArraySize - oldArraySize; i++) {
                    initialMergeSortArray.add(initialMergeSortArray.get(oldArraySize - 1) - i);
                }
            }
            if (oldArraySize > newArraySize) {
                for (int i = oldArraySize - 1; i >= newArraySize; i--) {
                    initialMergeSortArray.remove(i);
                }
            }
        }
    }

    private void configureArraySizeChoiceBox() {
        arraySizeChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> changeArraySize(newValue)
        );
        arraySizeChoiceBox.addEventFilter(KeyEvent.KEY_PRESSED, this::navigateArraySizeChoiceBox);
        arraySizeChoiceBox.setItems(FXCollections.observableArrayList(arraySizeOptions));
    }

    private void navigateArraySizeChoiceBox(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!arraySizeChoiceBox.isShowing()) {
                arraySizeChoiceBox.show();
            } else {
                arraySizeChoiceBox.hide();
            }
            event.consume();
        } else if (event.getCode() == KeyCode.ESCAPE && arraySizeChoiceBox.isShowing()) {
            arraySizeChoiceBox.hide();
            event.consume();
        }
    }

    private int calculateMergeSortDepth() {
        return (int) (Math.log(selectedArraySize) / Math.log(2)) + 1;
    }

    @Override
    public String getName() {
        return "Mergesort";
    }

    @Override
    public List<AlgorithmVariable<?>> getVariables() {
        return variables;
    }

    @Override
    public List<Command> getCommandHistory() {
        return commandHistory;
    }

    @Override
    public int getHighestCommandId() {
        return highestCommandId;
    }

    @SneakyThrows
    private void loadFxController() {
        var loader = new FXMLLoader(getClass().getResource("merge-sort.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @Override
    public void onCommandCopied(String commandExpression) {}
}
