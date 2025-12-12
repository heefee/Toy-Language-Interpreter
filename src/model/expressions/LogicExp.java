package model.expressions;

import exceptions.MyException;
import model.adt.IMyDictionary;
import model.adt.IMyHeap;
import model.types.BoolType;
import model.types.IType;
import model.types.IntType;
import model.values.BoolValue;
import model.values.IValue;

public class LogicExp implements IExp{
    private IExp e1;
    private IExp e2;
    public int op; //1-and, 2-or

    public LogicExp(IExp e1, IExp e2, int op){
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    @Override
    public IValue eval(IMyDictionary<String, IValue> dict, IMyHeap<Integer,IValue> heap) {
        IValue v1,v2;
        v1 = e1.eval(dict,heap);
        if(v1.getType().equals(new BoolType())){
            v2 = e2.eval(dict,heap);
            if(v2.getType().equals(new BoolType())){
                BoolValue b1 = (BoolValue) v1;
                BoolValue b2 = (BoolValue) v2;

                boolean n1,n2;
                n1 = b1.getValue();
                n2 = b2.getValue();
                if(op==1) return new BoolValue(n1&&n2);
                if(op==2) return new BoolValue(n1||n2);
            } else throw new MyException("Second operand is not a boolean");
        } else throw new MyException("First operand is not a boolean");

        return v1;
    }

    @Override
    public IExp deepCopy(){
        return new LogicExp(e1.deepCopy(),e2.deepCopy(),op);
    }

    public IType typecheck(IMyDictionary<String, IType> typeEnv) throws MyException {
        IType type1,type2;
        type1=e1.typecheck(typeEnv);
        type2=e2.typecheck(typeEnv);

        if(type1.equals(new BoolType())){
            if(type2.equals(new BoolType()))
                return new BoolType();
            else
                throw new MyException("second operand is not an integer");
        }
        else{
            throw new MyException("first operand is not an integer");
        }
    }
}
