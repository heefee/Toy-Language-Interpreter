package model;

import model.adt.*;
import model.statements.IStmt;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;

public class PrgState {
    private IMyStack<IStmt> exeStack;
    private IMyDictionary<String, IValue> symTable;
    private IMyList<IValue> out;

    private IMyDictionary<StringValue, BufferedReader> filetable;
    private IMyHeap heap;

    private IStmt originalProgram;

    public PrgState(IMyStack<IStmt> stk, IMyDictionary<String, IValue> symtbl, IMyList<IValue> ot,IMyDictionary<StringValue, BufferedReader> filetable, IMyHeap heap, IStmt prg){
        this.exeStack = stk;
        this.out = ot;
        this.symTable = symtbl;
        this.originalProgram = prg.deepCopy();
        stk.push(prg);
        this.filetable = filetable;
        this.heap = heap;
    }


    public IMyDictionary<StringValue,BufferedReader> getFiletable(){
        return this.filetable;
    }

    public void setFiletable(IMyDictionary<StringValue,BufferedReader> filetable1){
        this.filetable = filetable1;
    }

    public IMyStack<IStmt> getExeStack()
    {
        return exeStack;
    }

    public IMyDictionary<String, IValue> getSymTable()
    {
        return symTable;
    }

    public IMyList<IValue> getOut()
    {
        return out;
    }

    public IMyHeap getHeap(){
        return heap;
    }

    @Override
    public String toString() {
        return "ExeStack: " + exeStack + "\n" + "SymTable: " + symTable + "\n"+"Out: " + out + "\n"+"FileTable: " + filetable.toString()+"\n" +"Heap: " + heap.toString() + "\n";
    }
}
