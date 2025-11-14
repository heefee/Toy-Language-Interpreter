package model.values;

import model.types.IType;
import model.types.IntType;

public class IntValue implements IValue {
    private int value;

    public IntValue(int val){
        this.value = val;
    }

    @Override
    public IType getType(){
        return new IntType();
    }

    public int getValue(){
        return this.value;
    }

    @Override
    public String toString(){
        return String.valueOf(this.value);
    }

    @Override
    public IValue deepCopy(){
        return new IntValue(this.value);
    }
}
