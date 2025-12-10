package model;

import exceptions.MyException;
import model.adt.*;
import model.statements.IStmt;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;

public class PrgState {
    private IMyStack<IStmt> exeStack;
    private IMyDictionary<String, IValue> symTable;
    private IMyList<IValue> out;
    private int currentId;
    static int nextId = 0;
    private IMyDictionary<StringValue, BufferedReader> filetable;
    private IMyHeap heap;

    private IStmt originalProgram;

    public PrgState(int givenId, IMyStack<IStmt> stk, IMyDictionary<String, IValue> symtbl, IMyList<IValue> ot,IMyDictionary<StringValue, BufferedReader> filetable, IMyHeap heap, IStmt prg){
        this.currentId = givenId;
        this.exeStack = stk;
        this.out = ot;
        this.symTable = symtbl;
        this.originalProgram = prg.deepCopy();
        stk.push(prg);
        this.filetable = filetable;
        this.heap = heap;
        this.currentId = getNextId();
    }

    public static synchronized int getNextId() {
        return ++nextId;
    }

    public PrgState oneStep() throws MyException {
        if (this.exeStack.isEmpty()) throw new MyException("prgstate stack is empty");
        IStmt crtStmt = this.exeStack.pop();
        return crtStmt.execute(this);
    }

    public boolean isNotCompleted() {
        return !this.exeStack.isEmpty();
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
        return "ID: " + this.currentId + "\n" + "ExeStack: " + exeStack + "\n" + "SymTable: " + symTable + "\n"+"Out: " + out + "\n"+"FileTable: " + filetable.toString()+"\n" +"Heap: " + heap.toString() + "\n";
    }
}
