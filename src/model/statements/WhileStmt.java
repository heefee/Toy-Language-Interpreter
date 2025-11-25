package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.expressions.IExp;
import model.values.BoolValue;
import model.values.IValue;

public class WhileStmt implements IStmt{
    private IExp exp;
    private IStmt stmt;

    public WhileStmt(IExp exp, IStmt stmt){
        this.exp = exp;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue evalExp = exp.eval(state.getSymTable(), state.getHeap());
        if(!(evalExp instanceof BoolValue))
            throw new MyException("Expression in while not of bool type");

        if(((BoolValue) evalExp).getValue()){
            state.getExeStack().push(deepCopy());
            state.getExeStack().push(stmt);
        }

        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(exp.deepCopy(), stmt.deepCopy());
    }

    public String toString() {
        return "while(" + exp.toString() + ")" + stmt.toString();
    }
}
