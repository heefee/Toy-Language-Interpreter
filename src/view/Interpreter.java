package view;

import controller.Controller;
import model.PrgState;
import model.adt.*;
import model.expressions.*;
import model.statements.*;
import model.types.*;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;
import repository.IRepository;
import repository.Repository;
import model.expressions.IExp;
import model.statements.IStmt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Dictionary;

class Interpreter {

    public static void main(String[] args) throws IOException {
        IStmt ex1 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                ));

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

        //Example:
        // Ref int v
        // new(v,20);
        // Ref Ref int a;
        // new(a,v);
        // print(v);
        // print(a)
        //At the end of execution: Heap={1->20, 2->(1,int)}, SymTable={v->(1,int), a->(2,Ref int)} and Out={(1,int),(2,Ref int)}

        IStmt ex5 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())), new CompStmt(
                new NewStmt("v", new ValueExp(new IntValue(20))), new CompStmt(
                new VarDeclStmt("a", new RefType(new RefType(new IntType()))), new CompStmt(
                new NewStmt("a", new VarExp("v")), new CompStmt(
                new PrintStmt(new VarExp("v")), new PrintStmt(new VarExp("a")))))));

        //Example:
        // Ref int v;
        // new(v,20);
        // print(rH(v));
        // wH(v,30);
        // print(rH(v)+5);
        //At the end of execution: Heap={1->30}, SymTable={v->(1,int)} and Out={20, 35}

        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())), new CompStmt(
                new NewStmt("v", new ValueExp(new IntValue(20))), new CompStmt(
                new PrintStmt(new rH(new VarExp("v"))), new CompStmt(
                new wH("v", new ValueExp(new IntValue(30))),
                new PrintStmt(new ArithExp(new rH(new VarExp("v")), new ValueExp(new IntValue(5)),1))))));

        //int x;
        //x=5;
        //while(x>0){
        //  print(x);
        //  x=x-1;
        //}

        //1-<, 2-<=, 3-==, 4-!=, 5- >, 6- >=

        IStmt ex7 = new CompStmt(new VarDeclStmt("x", new IntType()), new CompStmt(
                new AssignStmt("x", new ValueExp(new IntValue(5))), new WhileStmt(
                new RelExp(new VarExp("x"), new ValueExp(new IntValue(0)),5),
                new CompStmt(new PrintStmt(new VarExp("x")),
                        new AssignStmt("x", new ArithExp(new VarExp("x"), new ValueExp(new IntValue(1)),2))))));

        //Example:
        //Ref int v;
        //new(v,20);
        //Ref Ref int a;
        //new(a,v);
        //new(v,30);
        //print(rH(rH(a)))

        IStmt ex8 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())), new CompStmt(
                new NewStmt("v", new ValueExp(new IntValue(20))), new CompStmt(
                new VarDeclStmt("a", new RefType(new RefType(new IntType()))), new CompStmt(
                new NewStmt("a", new VarExp("v")), new CompStmt(
                new NewStmt("v", new ValueExp(new IntValue(30))),
                new PrintStmt(new rH(new rH(new VarExp("a")))))))));

        //      Example:
//        int v;
//        Ref int a;
//        v=10;
//        new(a,22);
//        fork(wH(a,30);v=32;print(v);print(rH(a)));
//        print(v);
//        print(rH(a))
//      At the end:
//        Id=1
//        SymTable_1={v->10,a->(1,int)}
//        Id=10
//        SymTable_10={v->32,a->(1,int)}
//        Heap={1->30}
//        Out={10,30,32,30}

        IStmt ex9 = new CompStmt(new VarDeclStmt("v", new IntType()), new CompStmt(
                new VarDeclStmt("a", new RefType(new IntType())), new CompStmt(
                new AssignStmt("v", new ValueExp(new IntValue(10))), new CompStmt(
                new NewStmt("a", new ValueExp(new IntValue(22))), new CompStmt(
                new ForkStmt(new CompStmt(new wH("a", new ValueExp(new IntValue(30))), new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(32))), new CompStmt(
                        new PrintStmt(new VarExp("v")), new PrintStmt(new rH(new VarExp("a")))))
                )), new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new rH(new VarExp("a"))))
        )))));


        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        MyDictionary typeEnv1 = new MyDictionary();
        try{
            ex1.typecheck(typeEnv1);
            PrgState prg1 = new PrgState(PrgState.getNextId(), new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable(), new MyHeap(), ex1);
            Repository rep1 = new Repository(prg1,"logs/logFile1.txt");
            Controller ctr1 = new Controller(rep1);
            menu.addCommand(new RunExample("1", ex1.toString(), ctr1));
        } catch (Exception e){
            System.out.println("Program 1 didn't pass the type checker: " + e.getMessage());
        }

        MyDictionary typeEnv2 = new MyDictionary();
        try{
            ex2.typecheck(typeEnv2);
            PrgState prg2 = new PrgState(PrgState.getNextId(), new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable(), new MyHeap(), ex2);
            Repository rep2 = new Repository(prg2,"logs/logFile2.txt");
            Controller ctr2 = new Controller(rep2);
            menu.addCommand(new RunExample("2", ex2.toString(), ctr2));
        } catch (Exception e){
            System.out.println("Program 2 didn't pass the type checker: " + e.getMessage());
        }

        MyDictionary typeEnv3 = new MyDictionary();
        try{
            ex3.typecheck(typeEnv3);
            PrgState prg3 = new PrgState(PrgState.getNextId(), new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable(), new MyHeap(), ex3);
            Repository rep3 = new Repository(prg3,"logs/logFile3.txt");
            Controller ctr3 = new Controller(rep3);
            menu.addCommand(new RunExample("3", ex3.toString(), ctr3));
        } catch (Exception e){
            System.out.println("Program 3 didn't pass the type checker: " + e.getMessage());
        }


        MyDictionary typeEnv4 = new MyDictionary();
        try {
            ex4.typecheck(typeEnv4);
            PrgState prg4 = new PrgState(PrgState.getNextId(), new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable(), new MyHeap(), ex4);
            Repository rep4 = new Repository(prg4, "logs/logFile4.txt");
            Controller ctr4 = new Controller(rep4);
            menu.addCommand(new RunExample("4", ex4.toString(), ctr4));
        } catch (Exception e) {
            System.out.println("Program 4 didn't pass the type checker: " + e.getMessage());
        }

        MyDictionary typeEnv5 = new MyDictionary();
        try {
            ex5.typecheck(typeEnv5);
            PrgState prg5 = new PrgState(PrgState.getNextId(), new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable(), new MyHeap(), ex5);
            Repository rep5 = new Repository(prg5, "logs/logFile5.txt");
            Controller ctr5 = new Controller(rep5);
            menu.addCommand(new RunExample("5", ex5.toString(), ctr5));
        } catch (Exception e) {
            System.out.println("Program 5 didn't pass the type checker: " + e.getMessage());
        }

        MyDictionary typeEnv6 = new MyDictionary();
        try {
            ex6.typecheck(typeEnv6);
            PrgState prg6 = new PrgState(PrgState.getNextId(), new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable(), new MyHeap(), ex6);
            Repository rep6 = new Repository(prg6, "logs/logFile6.txt");
            Controller ctr6 = new Controller(rep6);
            menu.addCommand(new RunExample("6", ex6.toString(), ctr6));
        } catch (Exception e) {
            System.out.println("Program 6 didn't pass the type checker: " + e.getMessage());
        }

        MyDictionary typeEnv7 = new MyDictionary();
        try {
            ex7.typecheck(typeEnv7);
            PrgState prg7 = new PrgState(PrgState.getNextId(), new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable(), new MyHeap(), ex7);
            Repository rep7 = new Repository(prg7, "logs/logFile7.txt");
            Controller ctr7 = new Controller(rep7);
            menu.addCommand(new RunExample("7", ex7.toString(), ctr7));
        } catch (Exception e) {
            System.out.println("Program 7 didn't pass the type checker: " + e.getMessage());
        }

        MyDictionary typeEnv8 = new MyDictionary();
        try {
            ex8.typecheck(typeEnv8);
            PrgState prg8 = new PrgState(PrgState.getNextId(), new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable(), new MyHeap(), ex8);
            Repository rep8 = new Repository(prg8, "logs/logFile8.txt");
            Controller ctr8 = new Controller(rep8);
            menu.addCommand(new RunExample("8", ex8.toString(), ctr8));
        } catch (Exception e) {
            System.out.println("Program 8 didn't pass the type checker: " + e.getMessage());
        }

        MyDictionary typeEnv9 = new MyDictionary();
        try {
            ex9.typecheck(typeEnv9);
            PrgState prg9 = new PrgState(PrgState.getNextId(), new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable(), new MyHeap(), ex9);
            Repository rep9 = new Repository(prg9, "logs/logFile9.txt");
            Controller ctr9 = new Controller(rep9);
            menu.addCommand(new RunExample("9", ex9.toString(), ctr9));
        } catch (Exception e) {
            System.out.println("Program 9 didn't pass the type checker: " + e.getMessage());
        }

        menu.show();
    }
}