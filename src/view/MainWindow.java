package view;

import controller.Controller;
import exceptions.MyException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.PrgState;
import model.adt.IMyHeap;
import model.adt.IMyList;
import model.statements.IStmt;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainWindow {
    private final Controller controller;
    private Stage stage;
    private IMyList<IValue> lastOutput;

    private TextField nrPrgStatesField;
    private TableView<Map.Entry<Integer, IValue>> heapTableView;
    private ListView<String> outListView;
    private ListView<String> fileTableListView;
    private ListView<Integer> prgStateIdListView;
    private TableView<Map.Entry<String, IValue>> symTableView;
    private ListView<String> exeStackListView;
    private Button runOneStepButton;

    public MainWindow(Controller controller, IStmt selectedProgram) {
        this.controller = controller;
        createWindow(selectedProgram);
    }

    private void createWindow(IStmt selectedProgram) {
        stage = new Stage();
        stage.setTitle("Interpreter - " + selectedProgram.toString().substring(0, Math.min(50, selectedProgram.toString().length())) + "...");

        createComponents();

        GridPane mainLayout = new GridPane();
        mainLayout.setPadding(new Insets(10));
        mainLayout.setHgap(10);
        mainLayout.setVgap(10);

        HBox prgStateCountBox = new HBox(10);
        prgStateCountBox.getChildren().addAll(new Label("Number of PrgStates:"), nrPrgStatesField);
        mainLayout.add(prgStateCountBox, 0, 0, 2, 1);

        mainLayout.add(new VBox(5, new Label("Heap Table:"), heapTableView), 0, 1);
        mainLayout.add(new VBox(5, new Label("Output:"), outListView), 1, 1);
        mainLayout.add(new VBox(5, new Label("File Table:"), fileTableListView), 0, 2);
        mainLayout.add(new VBox(5, new Label("Program State IDs:"), prgStateIdListView), 1, 2);
        mainLayout.add(new VBox(5, new Label("Symbol Table:"), symTableView), 0, 3);
        mainLayout.add(new VBox(5, new Label("Execution Stack:"), exeStackListView), 1, 3);

        mainLayout.add(runOneStepButton, 0, 4, 2, 1);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> stage.close());
        mainLayout.add(backButton, 0, 5, 2, 1);

        populateAll();

        stage.setScene(new Scene(mainLayout, 900, 700));
    }

    private void createComponents() {
        nrPrgStatesField = new TextField();
        nrPrgStatesField.setEditable(false);

        heapTableView = new TableView<>();
        TableColumn<Map.Entry<Integer, IValue>, Integer> addrCol = new TableColumn<>("Address");
        addrCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getKey()).asObject());
        TableColumn<Map.Entry<Integer, IValue>, String> valCol = new TableColumn<>("Value");
        valCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getValue().toString()));
        heapTableView.getColumns().addAll(addrCol, valCol);

        outListView = new ListView<>();
        fileTableListView = new ListView<>();
        prgStateIdListView = new ListView<>();

        prgStateIdListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        populateSymTableAndExeStack(newVal);
                    }
                }
        );

        symTableView = new TableView<>();
        TableColumn<Map.Entry<String, IValue>, String> varCol = new TableColumn<>("Variable");
        varCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKey()));
        TableColumn<Map.Entry<String, IValue>, String> symValCol = new TableColumn<>("Value");
        symValCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getValue().toString()));
        varCol.setPrefWidth(150);
        symValCol.setPrefWidth(250);

        symTableView.getColumns().addAll(varCol, symValCol);

        exeStackListView = new ListView<>();

        runOneStepButton = new Button("Run One Step");
        runOneStepButton.setOnAction(e -> runOneStep());
    }

    private void populateAll() {
        List<PrgState> prgStates = controller.getPrgList();
        nrPrgStatesField.setText(String.valueOf(prgStates.size()));

        if (prgStates.isEmpty()) {
            if (lastOutput != null) {
                outListView.setItems(FXCollections.observableArrayList(
                        lastOutput.getAll().stream().map(Object::toString).toList()
                ));
            }
            heapTableView.getItems().clear();
            fileTableListView.getItems().clear();
            prgStateIdListView.getItems().clear();
            symTableView.getItems().clear();
            exeStackListView.getItems().clear();
            return;
        }

        PrgState first = prgStates.get(0);
        lastOutput = first.getOut();

        IMyHeap<Integer, IValue> heap = first.getHeap();
        heapTableView.getItems().setAll(heap.getContent().entrySet());
        heapTableView.refresh();

        outListView.setItems(FXCollections.observableArrayList(
                first.getOut().getAll().stream().map(Object::toString).toList()
        ));

        ObservableList<String> files = FXCollections.observableArrayList();
        for (StringValue sv : first.getFiletable().getContent().keySet()) {
            files.add(sv.toString());
        }
        fileTableListView.setItems(files);

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        for (PrgState p : prgStates) {
            ids.add(p.getId());
        }
        prgStateIdListView.setItems(ids);

        Integer selected = prgStateIdListView.getSelectionModel().getSelectedItem();
        if (selected == null && !ids.isEmpty()) {
            prgStateIdListView.getSelectionModel().selectFirst();
            selected = prgStateIdListView.getSelectionModel().getSelectedItem();
        }

        if (selected != null) {
            populateSymTableAndExeStack(selected);
        }
    }

    private void populateSymTableAndExeStack(Integer prgStateId) {
        PrgState state = controller.getPrgList().stream()
                .filter(p -> Objects.equals(p.getId(), prgStateId))
                .findFirst()
                .orElse(null);

        if (state == null) {
            symTableView.getItems().clear();
            exeStackListView.getItems().clear();
            return;
        }

        symTableView.getItems().setAll(state.getSymTable().getContent().entrySet());
        symTableView.refresh();

        ObservableList<String> stack = FXCollections.observableArrayList();
        List<IStmt> stmts = state.getExeStack().toList();
        for (int i = stmts.size() - 1; i >= 0; i--) {
            stack.add(stmts.get(i).toString());
        }
        exeStackListView.setItems(stack);
    }

    private void runOneStep() {
        if (controller.getPrgList().isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Program execution completed!").showAndWait();
            return;
        }

        try {
            controller.oneStepForAllPrgGUI();
            populateAll();
        } catch (MyException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    public void show() {
        stage.show();
    }
}
