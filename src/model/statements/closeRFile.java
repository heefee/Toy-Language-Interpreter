package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyDictionary;
import model.expressions.IExp;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class closeRFile implements IStmt{
    private IExp exp;

    public closeRFile(IExp exp){
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue value = exp.eval(state.getSymTable(),state.getHeap());
        if (!value.getType().equals(new StringType())) {
            throw new MyException(String.format("Close Read File Error: %s is not of type string.", exp));
        }

        StringValue fileName = (StringValue) value;
        IMyDictionary<StringValue, BufferedReader> fileTable = state.getFiletable();
        if (!fileTable.isDefined(fileName)){
            throw new MyException(String.format("Close Read File Error: Variable %s was not declared before.", value));
        }

        BufferedReader bufferedReader = fileTable.getValue(fileName);
        try {
            bufferedReader.close();
        } catch (IOException e) {
            throw new MyException(String.format("Close Read File Error: File %s could not be closed.", value));
        }

        fileTable.remove(fileName);

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new closeRFile(exp.deepCopy());
    }

    @Override
    public String toString(){
        return "closeRFile " + exp.toString() + ")";
    }
}
