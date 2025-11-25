package model.expressions;

import model.adt.IMyDictionary;
import model.adt.IMyHeap;
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

}
