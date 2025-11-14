package model.types;

import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;

public class StringType implements IType{
    public boolean equals(Object another){
        return another instanceof StringType;
    }

    @Override
    public IValue defaultValue() {
        return new StringValue("");
    }

    @Override
    public String toString(){
        return "string";
    }

    @Override
    public IType deepCopy(){
        return this;
    }
}
