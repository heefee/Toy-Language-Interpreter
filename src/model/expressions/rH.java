package model.expressions;

import exceptions.MyException;
import model.adt.IMyDictionary;
import model.adt.IMyHeap;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class rH implements IExp{        //Heap Reading
    private IExp exp;

    public rH(IExp exp){
        this.exp = exp;
    }

    @Override
    public IValue eval(IMyDictionary<String, IValue> dict, IMyHeap<Integer,IValue> heap) {
        IValue val = exp.eval(dict,heap);
        if(!(val instanceof RefValue)){
            throw new MyException("Heap Reading - Expression not of type reference");
        }
        RefValue refValue = (RefValue) val;
        return heap.get(refValue.getAddress());
    }

    @Override
    public IExp deepCopy() {
        return new rH(exp.deepCopy());
    }

    @Override
    public String toString(){
        return "rh(" + exp.toString() + ")";
    }
}
