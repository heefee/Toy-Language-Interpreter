package view;

import controller.Controller;
import model.PrgState;
import model.adt.*;
import model.expressions.ArithExp;
import model.expressions.IExp;
import model.expressions.ValueExp;
import model.expressions.VarExp;
import model.statements.*;
import model.types.BoolType;
import model.types.IntType;
import model.types.StringType;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;
import repository.IRepository;
import repository.Repository;

import java.io.BufferedReader;
import java.io.IOException;

class Interpreter {

    public static void main(String[] args) throws IOException {

        IStmt ex1 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                ));
        PrgState prg1 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), ex1);
        IRepository repo1 = new Repository(prg1, "logFile1.txt");
        Controller ctr1 = new Controller(repo1);

        IExp two = new ValueExp(new IntValue(2));
        IExp three = new ValueExp(new IntValue(3));
        IExp five = new ValueExp(new IntValue(5));
        IExp one = new ValueExp(new IntValue(1));
        IExp aVar = new VarExp("a");
        // 3 * 5
        IExp threeMulFive = new ArithExp(three, five, 3);
        // 2 + (3 * 5)
        IExp aRhs = new ArithExp(two, threeMulFive, 1);
        // a + 1
        IExp bRhs = new ArithExp(aVar, one, 1);
        IStmt ex2 = new CompStmt(
                new VarDeclStmt("a", new IntType()),
                new CompStmt(
                        new VarDeclStmt("b", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", aRhs),
                                new CompStmt(
                                        new AssignStmt("b", bRhs),
                                        new PrintStmt(new VarExp("b"))
                                )
                        )
                )
        );
        PrgState prg2 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), ex2);
        IRepository repo2 = new Repository(prg2,"logFile2.txt");
        Controller ctr2 = new Controller(repo2);

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
        PrgState prg3 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), ex3);
        IRepository repo3 = new Repository(prg3, "logFile3.txt");
        Controller ctr3 = new Controller(repo3);

        IStmt ex4 = new CompStmt(new VarDeclStmt("varf", new StringType()), new CompStmt(
                new AssignStmt("varf", new ValueExp(new StringValue("test.in"))), new CompStmt(
                new openRFile(new VarExp("varf")), new CompStmt(
                new VarDeclStmt("varc", new IntType()), new CompStmt(
                new readFile(new VarExp("varf"), "varc"), new CompStmt(
                new PrintStmt(new VarExp("varc")), new CompStmt(
                new readFile(new VarExp("varf"), "varc"), new CompStmt(
                new PrintStmt(new VarExp("varc")), new closeRFile(new VarExp("varf"))))))))));

        PrgState prg4 = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), ex4);
        IRepository repo4 = new Repository(prg4, "logFile4.txt");
        Controller ctr4 = new Controller(repo4);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), ctr1));
        menu.addCommand(new RunExample("2", ex2.toString(), ctr2));
        menu.addCommand(new RunExample("3", ex3.toString(), ctr3));
        menu.addCommand(new RunExample("4",ex4.toString(),ctr4));
        menu.show();
    }
}