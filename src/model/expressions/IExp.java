package model.expressions;

import model.adt.IMyDictionary;
import model.adt.IMyHeap;
import model.values.IValue;

public interface IExp {
    IValue eval (IMyDictionary<String, IValue> dict, IMyHeap<Integer,IValue> heap);
    IExp deepCopy();
}
