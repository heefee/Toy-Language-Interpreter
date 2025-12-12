package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyDictionary;
import model.expressions.IExp;
import model.types.BoolType;
import model.types.IType;
import model.types.StringType;
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

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(exp.deepCopy(), stmt.deepCopy());
    }

    public String toString() {
        return "while(" + exp.toString() + ")" + stmt.toString();
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IType typeexp = exp.typecheck(typeEnv);
        if (typeexp.equals(new BoolType())) {
            stmt.typecheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new MyException("WHILE: The condition of While doesnt have the right exp");
    }
}
