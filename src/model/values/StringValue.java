package model.values;

import model.types.IType;
import model.types.StringType;

public class StringValue implements IValue {
    private String str;

    public StringValue(String str){
        this.str = str;
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    public String getValue(){
        return this.str;
    }

    @Override
    public IValue deepCopy(){
        return new StringValue(str);
    }

    public String toString(){
        return str;
    }
}
