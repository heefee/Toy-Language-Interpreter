package model.expressions;

import exceptions.MyException;
import model.adt.IMyDictionary;
import model.adt.IMyHeap;
import model.types.IType;
import model.values.IValue;

public class VarExp implements IExp{
    private String id;

    public VarExp(String givenId){
        id = givenId;
    }

    @Override
    public IValue eval(IMyDictionary<String,IValue> dict, IMyHeap<Integer,IValue> heap){
        return dict.getValue(id);
    }

    @Override
    public IExp deepCopy() {
        return new VarExp(id);
    }

    public String toString(){
        return id;
    }

    public IType typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv.getType(id);
    }
}
