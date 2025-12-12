package model.expressions;

import exceptions.MyException;
import model.adt.IMyDictionary;
import model.adt.IMyHeap;
import model.types.IType;
import model.values.IValue;

import java.lang.reflect.Type;

public interface IExp {
    IValue eval (IMyDictionary<String, IValue> dict, IMyHeap<Integer,IValue> heap);
    IExp deepCopy();
    IType typecheck(IMyDictionary <String,IType> typeEnv) throws MyException;
}
