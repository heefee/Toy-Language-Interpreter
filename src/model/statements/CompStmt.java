package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIStack;

public class CompStmt implements IStmt{
    private IStmt first;
    private IStmt second;

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
        MyIStack<IStmt> stk = state.getExeStack();
        stk.push(second);
        stk.push(first);
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new CompStmt(this.first.deepCopy(),this.second.deepCopy());
    }
}
