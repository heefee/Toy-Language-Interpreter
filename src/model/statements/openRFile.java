package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.expressions.IExp;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class openRFile implements IStmt{
    private IExp exp;

    public openRFile(IExp exp){
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue value = exp.eval(state.getSymTable(),state.getHeap());
        if(!value.getType().equals(new StringType()))
            throw new MyException("The filename must be a string");

        StringValue value1 = (StringValue) value;
        if(state.getFiletable().isDefined(value1))
            throw new MyException("The file is already open!");
        try {
            state.getFiletable().put(value1, new BufferedReader(new FileReader(value1.getValue())));
        } catch (FileNotFoundException e){
            throw new MyException(e.getMessage());
        }

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new openRFile(this.exp.deepCopy());
    }

    @Override
    public String toString(){
        return "openRFile " + exp.toString() + ")";
    }
}
