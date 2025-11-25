package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyDictionary;
import model.expressions.IExp;
import model.types.IType;
import model.values.IValue;

public class AssignStmt implements IStmt{
    private String id;
    private IExp exp;

    public AssignStmt(String id, IExp exp){
        this.id = id;
        this.exp = exp;
    }

    @Override
    public String toString(){
        return id + "=" + exp.toString();
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IMyDictionary<String, IValue> symTable = state.getSymTable();
        if(!symTable.isDefined(this.id)) {
            throw new MyException("Assignment error: variable '" + this.id + "' was not declared");
        }
        IValue value = this.exp.eval(symTable,state.getHeap());
        IValue oldValue = symTable.getValue(this.id);
        IType typeId = oldValue.getType();
        if(!value.getType().equals(typeId)) {
            throw new MyException("Type mismatch in assignment to '" + this.id + "' expected " + typeId.toString() + " but got " + value.getType().toString());
        }
        symTable.put(this.id, value);
        return state;
    }

    @Override
    public IStmt deepCopy(){
        return new AssignStmt(this.id,this.exp.deepCopy());
    }
}
