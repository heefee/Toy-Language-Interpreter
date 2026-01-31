package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyDictionary;
import model.expressions.IExp;
import model.types.BoolType;
import model.types.IType;

public class ForStmt implements IStmt{
    private IStmt initialVal;
    private IExp comparisonExp;
    private IStmt increment;
    private IStmt actionStmt;

    public ForStmt(IStmt initialVal, IExp comparisonExp, IStmt increment, IStmt actionStmt){
        this.initialVal=initialVal;
        this.comparisonExp=comparisonExp;
        this.increment=increment;
        this.actionStmt=actionStmt;
    }


    @Override
    public PrgState execute(PrgState state) throws MyException {
        IStmt stmt1 = initialVal;
        WhileStmt stmt2 = new WhileStmt(comparisonExp,new CompStmt(actionStmt,increment));
        CompStmt newStmt = new CompStmt(stmt1, stmt2);
        state.getExeStack().push(newStmt);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new ForStmt(initialVal.deepCopy(),comparisonExp.deepCopy(),increment.deepCopy(),actionStmt.deepCopy());
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IType comparisonExpType = comparisonExp.typecheck(typeEnv);
        if (!comparisonExpType.equals(new BoolType())){
            throw new MyException("TYPE CHECK ERROR: The condition of FOR is not of type bool.");
        }
        initialVal.typecheck(typeEnv);
        increment.typecheck(typeEnv);
        actionStmt.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }

    public String toString(){
        return "for(" + initialVal.toString()+"; " + comparisonExp.toString() + "; " + increment.toString()+") " + actionStmt.toString();
    }
}
