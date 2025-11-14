package model.expressions;

import model.adt.MyIDictionary;
import model.values.IValue;

public class VarExp implements IExp{
    private String id;

    public VarExp(String givenId){
        id = givenId;
    }

    @Override
    public IValue eval(MyIDictionary<String,IValue> dict){
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
