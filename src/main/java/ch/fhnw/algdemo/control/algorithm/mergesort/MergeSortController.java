package ch.fhnw.algdemo.control.algorithm.mergesort;

import ch.fhnw.algdemo.control.MainController;
import ch.fhnw.algdemo.model.algorithm.AlgorithmController;
import ch.fhnw.algdemo.model.algorithm.AlgorithmVariable;
import ch.fhnw.algdemo.model.command.Command;
import ch.fhnw.algdemo.model.algorithm.IntegerAlgorithmVariable;
import ch.fhnw.algdemo.model.command.MergeSortCommand;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private MergeSortTreeState treeState;

    private final List<Command> commandHistory = new ArrayList<>();
    private final ObservableList<MergeSortCommand> fullCommandHistory = FXCollections.observableArrayList();
    private final SimpleIntegerProperty selectedCommandId = new SimpleIntegerProperty(1);
    private final SimpleObjectProperty<MergeSortCommand> selectedCommand = new SimpleObjectProperty<>();
    private MergeSortCommand initialCommand;
    private int highestCommandId = 0;

    private final List<AlgorithmVariable<?>> variables = List.of(
            new IntegerAlgorithmVariable("someName", null)
    );

    private final ObservableList<String> arraySizeOptions = FXCollections.observableArrayList("4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16");
    private Integer selectedArraySize = 16;

    private final List<Integer> initialMergeSortArray = new ArrayList<>(List.of(16, 15, 14, 13, 12 ,11 ,10 ,9, 8, 7, 6, 5, 4, 3, 2, 1));

    private final SimpleBooleanProperty stopClicked = new SimpleBooleanProperty(true);

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
    }

    @FXML
    public void initialize() {
        configureArraySizeChoiceBox();
        configureNavigationButtons();
        arraySizeChoiceBox.getSelectionModel().select(4);
    }

    @Override
    public void updateAlgorithmState(int selectedCommandId) {
        if (selectedCommandId == 0) {
            this.selectedCommand.set(initialCommand);
        } else {
            var command = commandHistory.get(selectedCommandId - 1);
            if (command instanceof MergeSortCommand msc) {
                selectedCommand.set(msc);
            }
        }
        this.selectedCommandId.set(selectedCommandId);
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
    public int getSelectedCommandId() {
        return selectedCommandId.getValue();
    }

    private void initializeMergeSort() {
        mergeSortGridPane.getChildren().clear();
        mergeSortGridPane.getRowConstraints().clear();
        var mergeSortDepth = calculateMergeSortDepth();

        for (int i = 0; i < mergeSortDepth; i++) {
            var rc = new RowConstraints();
            rc.setPercentHeight(100.0 / mergeSortDepth);
            mergeSortGridPane.getRowConstraints().add(rc);

            var row = new MergeSortRow(selectedCommand, selectedArraySize, i, mergeSortDepth);
            mergeSortGridPane.add(row, 0, i);
        }
    }

    private void changeArraySize(String newValue) {
        var newArraySize = Integer.parseInt(newValue);
        /*changeArraySizes(newArraySize);*/
        selectedArraySize = newArraySize;

        stopClicked.set(true);
        generateHistory();
        updateAlgorithmState(0);
        initializeMergeSort();
        mainController.applyCommand();
    }

/*    private void changeArraySizes(int newArraySize) {
        if (selectedArraySize != newArraySize) {
            if (selectedArraySize < newArraySize) {
                for (int i = 0; i < newArraySize - selectedArraySize; i++) {
                    updatedMergeSortArray.add(new SimpleIntegerProperty(initialMergeSortArray.get(selectedArraySize - i)));
                }
            }
            if (selectedArraySize > newArraySize) {
                for (int i = selectedArraySize - 1; i >= newArraySize; i--) {
                    updatedMergeSortArray.remove(i);
                }
            }
        }
    }*/

    private void generateHistory() {
        fullCommandHistory.clear();
        commandHistory.clear();

        var a = new ArrayList<>(initialMergeSortArray.subList(0, selectedArraySize));
        treeState = new MergeSortTreeState(calculateMergeSortDepth(), a);

        highestCommandId = 0;
        selectedCommandId.set(0);

        initialCommand = new MergeSortCommand("Initial State", selectedCommandId.getValue(), treeState.getNumbers(),
                treeState.getVisibilities(), treeState.getHighlights());

        sort(a, 0, selectedArraySize, 0);
    }

    private void sort(List<Integer> a, int beg, int end, int row) {
        treeState.touch(row, beg, end);
        recordSnapshot("sort(a, " + beg + ", " + end + ")");

        if (end - beg > 1) {
            int m = (beg + end) / 2;
            sort(a, beg, m, row + 1);
            sort(a, m, end, row + 1);
            merge(a, beg, m, end, row);
        }
    }

    private void merge(List<Integer> a, int beg, int m, int end, int row) {
        int i = 0, j = beg, k = m;
        var b = new int[end - beg];
        int childRow = row + 1;

        while (i < end - beg) {
            if (k == end || (j < m && compare(a, j, k, childRow))) {
                b[i++] = a.get(j++);
            } else {
                b[i++] = a.get(k++);
            }
        }

        i = 0;
        for (int y = beg; y < end; y++) a.set(y, b[i++]);


        treeState.completeMerge(row, beg, end, a);
        recordSnapshot("merge(a, " + beg + ", " + m + ", " + end + ")");
    }

    private boolean compare(List<Integer> a, int j, int k, int childRow) {
        treeState.clearHighlights(childRow);
        treeState.highlight[childRow][j] = true;
        treeState.highlight[childRow][k] = true;
        recordSnapshot("a[" + j + "] < a[" + k + "]");
        return a.get(j) < a.get(k);
    }

    private void recordSnapshot(String description) {
        selectedCommandId.set(selectedCommandId.getValue() + 1);
        var cmd = new MergeSortCommand(description, selectedCommandId.getValue(), treeState.getNumbers(),
                treeState.getVisibilities(), treeState.getHighlights());
        fullCommandHistory.add(cmd);
    }

    private int calculateMergeSortDepth() {
        var depth = (int) (Math.log(selectedArraySize) / Math.log(2)) + 1;
        return !isPowerOfTwo(selectedArraySize) ? depth + 1 : depth;
    }

    private boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    private void configureNavigationButtons() {
        startButton.setOnMouseClicked(e -> onStartClicked(null));
        startButton.setOnKeyPressed(this::onStartClicked);
        startButton.visibleProperty().bind(stopClicked);
        startButton.managedProperty().bind(stopClicked);
        stopButton.setOnMouseClicked(e -> onStopClicked(null));
        stopButton.setOnKeyPressed(this::onStopClicked);
        prevButton.setOnMouseClicked(e -> onPrevClicked(null));
        prevButton.setOnKeyPressed(this::onPrevClicked);
        prevButton.visibleProperty().bind(stopClicked.not());
        prevButton.managedProperty().bind(stopClicked.not());
        prevButton.disableProperty().bind(selectedCommandId.isEqualTo(0));
        nextButton.setOnMouseClicked(e -> onNextClicked(null));
        nextButton.setOnKeyPressed(this::onNextClicked);
        nextButton.visibleProperty().bind(stopClicked.not());
        nextButton.managedProperty().bind(stopClicked.not());
        nextButton.disableProperty().bind(Bindings.equal(selectedCommandId, Bindings.size(fullCommandHistory)));
    }

    private void onStartClicked(KeyEvent event) {
        if (event == null || event.getCode() == KeyCode.ENTER) {
            stopClicked.set(false);
            onNextClicked(null);
        }
    }

    private void onStopClicked(KeyEvent event) {
        if (event == null || event.getCode() == KeyCode.ENTER) {
            stopClicked.set(true);
            commandHistory.clear();
            highestCommandId = 0;
            updateAlgorithmState(0);
            mainController.applyCommand();
        }
    }

    private void onPrevClicked(KeyEvent event) {
        if (event == null || event.getCode() == KeyCode.ENTER) {
            updateAlgorithmState(selectedCommandId.getValue() - 1);
            mainController.applyCommand();
        }
    }

    private void onNextClicked(KeyEvent event) {
        if (event == null || event.getCode() == KeyCode.ENTER) {
            int nextCommandId;
            if (selectedCommandId.getValue().equals(highestCommandId) && selectedCommandId.getValue() < fullCommandHistory.size()) {
                var command = fullCommandHistory.get(selectedCommandId.getValue());
                commandHistory.add(command);
                highestCommandId = command.getId();
                nextCommandId = command.getId();
            } else {
                nextCommandId = commandHistory.get(selectedCommandId.getValue()).getId();
            }
            updateAlgorithmState(nextCommandId);
            mainController.applyCommand();
        }
    }

    private void configureArraySizeChoiceBox() {
        arraySizeChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> changeArraySize(newValue)
        );
        arraySizeChoiceBox.addEventFilter(KeyEvent.KEY_PRESSED, this::navigateArraySizeChoiceBox);
        arraySizeChoiceBox.setItems(arraySizeOptions);
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
