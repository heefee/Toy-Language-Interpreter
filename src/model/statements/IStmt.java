package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyDictionary;
import model.types.IType;

public interface IStmt {
    PrgState execute (PrgState state) throws MyException;
    IStmt deepCopy();
    IMyDictionary<String, IType> typecheck(IMyDictionary<String,IType> typeEnv) throws MyException;
}
