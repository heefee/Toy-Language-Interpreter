package view;

import controller.Controller;
import exceptions.MyException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.PrgState;
import model.adt.*;
import model.expressions.*;
import model.statements.*;
import model.types.*;
import model.values.*;
import repository.IRepository;
import repository.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class GUI extends Application {
    private ListView<IStmt> programListView;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Select Program to Execute");

        // Create ListView for programs
        programListView = new ListView<>();
        programListView.setItems(getExamplePrograms());
        programListView.setPrefHeight(400);

        // Create Select Button
        Button selectButton = new Button("Select Program");
        selectButton.setOnAction(e -> {
            IStmt selected = programListView.getSelectionModel().getSelectedItem();
            int selectedIndex = programListView.getSelectionModel().getSelectedIndex();
            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a program!");
                alert.showAndWait();
            } else {
                try {
                    selected.typecheck(new MyDictionary<>());

                    IMyStack<IStmt> exeStack = new MyStack<>();
                    IMyDictionary<String, IValue> symTable = new MyDictionary<>();
                    IMyList<IValue> out = new MyList<>();
                    IMyDictionary<StringValue, BufferedReader> fileTable = new MyFileTable<>();
                    IMyHeap<Integer, IValue> heap = new MyHeap();

                    PrgState initialState = new PrgState(PrgState.getNextId(), exeStack, symTable, out, fileTable, heap, selected);

                    String logFilePath = "logs/logFile" + (selectedIndex + 1) + ".txt";
                    IRepository repo = new Repository(initialState, logFilePath);
                    Controller controller = new Controller(repo);

                    MainWindow mainWindow = new MainWindow(controller, selected);
                    mainWindow.show();

                } catch (MyException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Type check failed: " + ex.getMessage());
                    alert.showAndWait();
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "IO Error: " + ex.getMessage());
                    alert.showAndWait();
                }
            }
        });

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e ->{
            Platform.exit();
            System.exit(0);
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(programListView, selectButton, exitButton);

        Scene scene = new Scene(layout, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private ObservableList<IStmt> getExamplePrograms() {
        List<IStmt> examples = new ArrayList<>();

        // Example 1: int v; v=2; Print(v)
        IStmt ex1 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                )
        );

        // Example 2: int a; int b; a=2+3*5; b=a+1; Print(b)
        IStmt ex2 = new CompStmt(
                new VarDeclStmt("a", new IntType()),
                new CompStmt(
                        new VarDeclStmt("b", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ArithExp(
                                        new ValueExp(new IntValue(2)),
                                        new ArithExp(
                                                new ValueExp(new IntValue(3)),
                                                new ValueExp(new IntValue(5)),
                                                3
                                        ),
                                        1
                                )),
                                new CompStmt(
                                        new AssignStmt("b", new ArithExp(
                                                new VarExp("a"),
                                                new ValueExp(new IntValue(1)),
                                                1
                                        )),
                                        new PrintStmt(new VarExp("b"))
                                )
                        )
                )
        );

        // Example 3: bool a; int v; a=true; (if a then v=2 else v=3); Print(v)
        IStmt ex3 = new CompStmt(
                new VarDeclStmt("a", new BoolType()),
                new CompStmt(
                        new VarDeclStmt("v", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(
                                        new IfStmt(
                                                new VarExp("a"),
                                                new AssignStmt("v", new ValueExp(new IntValue(2))),
                                                new AssignStmt("v", new ValueExp(new IntValue(3)))
                                        ),
                                        new PrintStmt(new VarExp("v"))
                                )
                        )
                )
        );

        IStmt ex4 = new CompStmt(new VarDeclStmt("varf", new StringType()), new CompStmt(
                new AssignStmt("varf", new ValueExp(new StringValue("input/test.in"))), new CompStmt(
                new openRFile(new VarExp("varf")), new CompStmt(
                new VarDeclStmt("varc", new IntType()), new CompStmt(
                new readFile(new VarExp("varf"), "varc"), new CompStmt(
                new PrintStmt(new VarExp("varc")), new CompStmt(
                new readFile(new VarExp("varf"), "varc"), new CompStmt(
                new PrintStmt(new VarExp("varc")), new closeRFile(new VarExp("varf"))))))))));

        IStmt ex5 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())), new CompStmt(
                new NewStmt("v", new ValueExp(new IntValue(20))), new CompStmt(
                new VarDeclStmt("a", new RefType(new RefType(new IntType()))), new CompStmt(
                new NewStmt("a", new VarExp("v")), new CompStmt(
                new PrintStmt(new VarExp("v")), new PrintStmt(new VarExp("a")))))));

        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())), new CompStmt(
                new NewStmt("v", new ValueExp(new IntValue(20))), new CompStmt(
                new PrintStmt(new rH(new VarExp("v"))), new CompStmt(
                new wH("v", new ValueExp(new IntValue(30))),
                new PrintStmt(new ArithExp(new rH(new VarExp("v")), new ValueExp(new IntValue(5)),1))))));

        IStmt ex7 = new CompStmt(new VarDeclStmt("x", new IntType()), new CompStmt(
                new AssignStmt("x", new ValueExp(new IntValue(5))), new WhileStmt(
                new RelExp(new VarExp("x"), new ValueExp(new IntValue(0)),5),
                new CompStmt(new PrintStmt(new VarExp("x")),
                        new AssignStmt("x", new ArithExp(new VarExp("x"), new ValueExp(new IntValue(1)),2))))));

        IStmt ex8 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())), new CompStmt(
                new NewStmt("v", new ValueExp(new IntValue(20))), new CompStmt(
                new VarDeclStmt("a", new RefType(new RefType(new IntType()))), new CompStmt(
                new NewStmt("a", new VarExp("v")), new CompStmt(
                new NewStmt("v", new ValueExp(new IntValue(30))),
                new PrintStmt(new rH(new rH(new VarExp("a")))))))));

        IStmt ex9 = new CompStmt(new VarDeclStmt("v", new IntType()), new CompStmt(
                new VarDeclStmt("a", new RefType(new IntType())), new CompStmt(
                new AssignStmt("v", new ValueExp(new IntValue(10))), new CompStmt(
                new NewStmt("a", new ValueExp(new IntValue(22))), new CompStmt(
                new ForkStmt(new CompStmt(new wH("a", new ValueExp(new IntValue(30))), new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(32))), new CompStmt(
                        new PrintStmt(new VarExp("v")), new PrintStmt(new rH(new VarExp("a")))))
                )), new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new rH(new VarExp("a"))))
        )))));

        IStmt ex10 =
                new CompStmt(
                  new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(
                                new NewStmt("a",new ValueExp(new IntValue(20))),
                                new CompStmt(
                                        new VarDeclStmt("v",new IntType()),
                                        new CompStmt(
                                                new ForStmt(
                                                        new AssignStmt("v",new ValueExp(new IntValue(0))),
                                                        new RelExp(new VarExp("v"),new ValueExp(new IntValue(3)),1),
                                                        new AssignStmt("v", new ArithExp(new VarExp("v"),new ValueExp(new IntValue(1)),1)),
                                                        new ForkStmt(
                                                                new CompStmt(
                                                                        new PrintStmt(new VarExp("v")),
                                                                        new AssignStmt("v",
                                                                                new ArithExp(new VarExp("v"), new rH(new VarExp("a")), 3))
                                                                )
                                                        )
                                                ),
                                                new PrintStmt(new rH(new VarExp("a")))
                                        )
                                )
                        )
                );

        examples.add(ex1);
        examples.add(ex2);
        examples.add(ex3);
        examples.add(ex4);
        examples.add(ex5);
        examples.add(ex6);
        examples.add(ex7);
        examples.add(ex8);
        examples.add(ex9);
        examples.add(ex10);

        return FXCollections.observableArrayList(examples);
    }
}