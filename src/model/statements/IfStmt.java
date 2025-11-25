package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyDictionary;
import model.expressions.IExp;
import model.types.BoolType;
import model.values.BoolValue;
import model.values.IValue;

public class IfStmt implements IStmt{
    private IExp exp;
    private IStmt thenS;
    private IStmt elseS;

    public IfStmt(IExp exp, IStmt thenS, IStmt elseS){
        this.exp = exp;
        this.thenS = thenS;
        this.elseS = elseS;
    }

    @Override
    public IStmt deepCopy(){
        return new IfStmt(this.exp.deepCopy(),this.thenS.deepCopy(),this.elseS.deepCopy());
    }

    @Override
    public PrgState execute (PrgState state) throws MyException{
        IMyDictionary<String, IValue> dict = state.getSymTable();
        if(exp.eval(dict, state.getHeap()).getType() instanceof BoolType){
            BoolValue bv = (BoolValue) exp.eval(dict,state.getHeap());
            if(bv.getValue())
                state.getExeStack().push(thenS);
            else
                state.getExeStack().push(elseS);
        } else{
            throw new MyException("Conditional expression is not boolean");
        }

        return state;
    }

    @Override
    public String toString(){
        return "IF (" + exp.toString() + ") THEN (" + thenS.toString() + ") ELSE (" + elseS.toString()+"))";
    }
}
