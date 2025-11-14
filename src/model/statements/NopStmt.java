package model.statements;

import exceptions.MyException;
import model.PrgState;

public class NopStmt implements IStmt{
    @Override
    public PrgState execute (PrgState state) throws MyException{
        return state;
    }

    @Override
    public IStmt deepCopy(){
        return new NopStmt();
    }

    @Override
    public String toString(){
        return "\n";
    }
}
