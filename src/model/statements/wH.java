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

public class wH implements IStmt{           //Heap Writing
    private String var_name;
    private IExp exp;

    public wH(String var_name, IExp exp){
        this.var_name = var_name;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IMyDictionary<String, IValue> symTable = state.getSymTable();
        IMyHeap heap = state.getHeap();

        if(!symTable.isDefined(var_name)){
            throw new MyException("Heap Writing - Cannot allocate memory to non existing variable");
        }
        IValue varValue = symTable.getValue(var_name);
        if(!(varValue instanceof RefValue)){
            throw new MyException("Heap Writing - var_name is not of Reference type");
        }

        RefValue refValue = (RefValue) varValue;
        IValue evalExp = exp.eval(symTable,heap);
        if(!evalExp.getType().equals(refValue.getLocationType())){
            throw new MyException("Heap Writing - evaluated exp is not of the type of the refValue");
        }

        heap.update(refValue.getAddress(),evalExp);

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new wH(var_name,exp.deepCopy());
    }

    @Override
    public String toString(){
        return "wH(" + var_name + "," + exp.toString() + ")";
    }
}
