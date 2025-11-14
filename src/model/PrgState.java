package model;

import model.adt.*;
import model.statements.IStmt;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.sql.Struct;

public class PrgState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, IValue> symTable;
    private MyIList<IValue> out;

    private MyIDictionary<StringValue, BufferedReader> filetable;

    private IStmt originalProgram;

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, IValue> symtbl,MyIList<IValue> ot, IStmt prg){
        this.exeStack = stk;
        this.out = ot;
        this.symTable = symtbl;
        this.originalProgram = prg.deepCopy();
        stk.push(prg);
        this.filetable = new MyFileTable<>();
    }


    public MyIDictionary<StringValue,BufferedReader> getFiletable(){
        return this.filetable;
    }

    public void setFiletable(MyIDictionary<StringValue,BufferedReader> filetable1){
        this.filetable = filetable1;
    }

    public MyIStack<IStmt> getExeStack()
    {
        return exeStack;
    }

    public MyIDictionary<String, IValue>  getSymTable()
    {
        return symTable;
    }

    public MyIList<IValue> getOut()
    {
        return out;
    }

    @Override
    public String toString() {
        return "ExeStack: " + exeStack + "\n" + "SymTable: " + symTable + "\n"+"Out: " + out + "\n"+"FileTable: " + filetable.toString()+"\n";
    }
}
