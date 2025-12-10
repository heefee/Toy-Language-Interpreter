package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyStack;

public class CompStmt implements IStmt{
    private IStmt first;
    private IStmt second;
    //aa
    public CompStmt(IStmt first, IStmt second){
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString(){
        return "(" + first.toString() + ";" + second.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException{
        IMyStack<IStmt> stk = state.getExeStack();
        stk.push(second);
        stk.push(first);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new CompStmt(this.first.deepCopy(),this.second.deepCopy());
    }
}
