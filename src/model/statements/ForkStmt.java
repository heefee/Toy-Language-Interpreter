package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyDictionary;
import model.adt.MyStack;
import model.types.IType;

public class ForkStmt implements IStmt {

    private IStmt innerStmt;

    public ForkStmt(IStmt innerStmt) {
        this.innerStmt = innerStmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        return new PrgState(PrgState.getNextId(), new MyStack<>(), state.getSymTable().deepCopy(), state.getOut(), state.getFiletable(), state.getHeap(), innerStmt);
    }

    @Override
    public IStmt deepCopy() {
        return new ForkStmt(this.innerStmt.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + this.innerStmt.toString() + ")";
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        return innerStmt.typecheck(typeEnv);
    }
}
