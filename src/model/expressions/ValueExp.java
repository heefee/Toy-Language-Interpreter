package model.expressions;

import exceptions.MyException;
import model.adt.IMyDictionary;
import model.adt.IMyHeap;
import model.types.IType;
import model.values.IValue;

public class ValueExp implements IExp {
    private IValue value;

    public ValueExp(IValue newValue) {
        value = newValue;
    }

    @Override
    public IValue eval(IMyDictionary<String, IValue> dict, IMyHeap<Integer, IValue> heap) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public IExp deepCopy() {
        return new ValueExp(value.deepCopy());
    }

    public IType typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        return value.getType();
    }
}
