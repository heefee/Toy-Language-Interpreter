package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyDictionary;
import model.adt.IMyHeap;
import model.expressions.IExp;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class NewStmt implements IStmt{                  //Heap Allocation
    private String var_name;
    private IExp exp;

    public NewStmt(String var_name, IExp exp){
        this.var_name = var_name;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IMyDictionary<String, IValue> symTable = state.getSymTable();
        IMyHeap heap = state.getHeap();
        if(!symTable.isDefined(var_name)){
            throw new MyException("Heap Alloc - Cannot allocate memory to non existing variable");
        }
        IValue varValue = symTable.getValue(var_name);
        if(!(varValue instanceof RefValue)){
            throw new MyException("Heap Alloc - var_name is not of Reference type");
        }

        IValue evalExp = exp.eval(symTable,heap);
        IType locationType = ((RefValue) varValue).getLocationType();
        if(!locationType.equals(evalExp.getType()))
            throw new MyException("Wrong type association");

        Integer newPos = heap.add(evalExp);
        symTable.put(var_name, new RefValue((Integer) heap.getLastKey(),locationType));

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new NewStmt(var_name,exp.deepCopy());
    }

    @Override
    public String toString(){
        return "new(" + var_name + "," + exp.toString() + ")";
    }

    @Override
    public IMyDictionary<String, IType> typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IType typevar = typeEnv.getType(var_name);
        IType typeexp = exp.typecheck(typeEnv);
        if(typevar.equals(new RefType(typeexp))) return typeEnv;
        else throw new MyException("NEW stmt: right hand side and left hand side have different types");
    }
}
