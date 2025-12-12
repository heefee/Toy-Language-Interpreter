package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyDictionary;
import model.types.IType;

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

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }
}
