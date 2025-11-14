package model.values;

import model.types.BoolType;
import model.types.IType;

public class BoolValue implements IValue{
    private boolean bool;

    public BoolValue(boolean b){
        this.bool = b;
    }

    @Override
    public IType getType(){
        return new BoolType();
    }


    public boolean getValue(){
        return this.bool;
    }

    @Override
    public String toString(){
        return String.valueOf(this.bool);
    }

    @Override
    public IValue deepCopy(){
        return new BoolValue(this.bool);
    }
}
