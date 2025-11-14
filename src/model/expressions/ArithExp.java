package model.expressions;

import exceptions.MyException;
import model.adt.MyIDictionary;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;

public class ArithExp implements IExp{
    private IExp e1;
    private IExp e2;
    private int op; //1-plus, 2-minus, 3-star, 4-divide

    public ArithExp(IExp e1, IExp e2, int op){
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> dict) throws MyException{
        IValue v1,v2;
        v1 = e1.eval(dict);
        if(v1.getType().equals(new IntType())){
            v2 = e2.eval(dict);
            if(v2.getType().equals(new IntType())){
                IntValue i1 = (IntValue) v1;
                IntValue i2 = (IntValue) v2;
                int n1,n2;
                n1 = i1.getValue();
                n2 = i2.getValue();
                if(op == 1) return new IntValue(n1+n2);
                if(op == 2) return new IntValue(n1-n2);
                if(op == 3) return new IntValue(n1*n2);
                if(op == 4)
                    if(n2 == 0) throw new MyException("Invalid operation - division by zero");
                    else return new IntValue(n1/n2);
            } else throw new MyException("Second operand is not an integer");
        } else throw new MyException("First operand is not an integer");
        return v1;
    }

    @Override
    public IExp deepCopy(){
        return new ArithExp(e1.deepCopy(),e2.deepCopy(),op);
    }
}
