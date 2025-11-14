package model.expressions;

import model.PrgState;
import model.adt.MyIDictionary;
import model.values.IValue;

public interface IExp {
    IValue eval (MyIDictionary<String, IValue> dict);
    IExp deepCopy();
}
