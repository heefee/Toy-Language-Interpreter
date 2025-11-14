package model.expressions;

import model.adt.MyIDictionary;
import model.values.IValue;

public class ValueExp implements IExp{
    private IValue value;
    public ValueExp(IValue newValue){
        value = newValue;
    }

    @Override
    public IValue eval (MyIDictionary<String,IValue> dict){
        return value;
    }

    @Override
    public String toString(){
        return value.toString();
    }

    @Override
    public IExp deepCopy(){
        return new ValueExp(value.deepCopy());
    }
}
