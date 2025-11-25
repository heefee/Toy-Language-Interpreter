package model.expressions;

import exceptions.MyException;
import model.adt.IMyDictionary;
import model.adt.IMyHeap;
import model.types.IntType;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class RelExp implements IExp{
    private IExp e1;
    private IExp e2;
    private int op; //1-<, 2-<=, 3-==, 4-!=, 5- >, 6- >=

    public RelExp(IExp e1, IExp e2, int op){
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    @Override
    public IValue eval(IMyDictionary<String, IValue> dict, IMyHeap<Integer,IValue> heap) throws MyException {
        IValue v1,v2;
        v1 = e1.eval(dict,heap);
        if(v1.getType().equals(new IntType())){
            v2 = e2.eval(dict,heap);
            if(v2.getType().equals(new IntType())){
                IntValue i1 = (IntValue) v1;
                IntValue i2 = (IntValue) v2;
                int n1,n2;
                n1 = i1.getValue();
                n2 = i2.getValue();
                /*
                if(op == 1) return new BoolValue(n1<n2);
                if(op == 2) return new BoolValue(n1<=n2);
                if(op == 3) return new BoolValue(n1==n2);
                if(op == 4) return new BoolValue(n1!=n2);
                if(op == 5) return new BoolValue(n1>n2);
                if(op == 6) return new BoolValue(n1>=n2);
                 */
                switch (op){
                    case 1:
                        return new BoolValue(n1<n2);
                    case 2:
                        return new BoolValue(n1<=n2);
                    case 3:
                        return new BoolValue(n1==n2);
                    case 4:
                        return new BoolValue(n1!=n2);
                    case 5:
                        return new BoolValue(n1>n2);
                    case 6:
                        return new BoolValue(n1>=n2);
                    default:
                        throw new MyException("Unknown operand");
                }
            } else throw new MyException("Second operand is not an integer");
        } else throw new MyException("First operand is not an integer");
        //return v1;
    }

    @Override
    public IExp deepCopy(){
        return new RelExp(e1.deepCopy(),e2.deepCopy(),op);
    }

    @Override
    public String toString(){
        return "RelExp(" + e1 + op + e2 + ")";
    }
}
