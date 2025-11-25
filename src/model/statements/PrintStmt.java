package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyList;
import model.expressions.IExp;
import model.values.IValue;

import static java.lang.IO.print;

public class PrintStmt implements IStmt{
    private IExp exp;

    public PrintStmt(IExp exp){
        this.exp = exp;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        print(exp.eval(state.getSymTable(), state.getHeap()));
        print("\n\n");
        IMyList<IValue> out = state.getOut();
        out.add(exp.eval(state.getSymTable(),state.getHeap()));
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(exp.deepCopy());
    }

    @Override
    public String toString(){
        return "Print " + exp.toString();
    }
}
