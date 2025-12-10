package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyDictionary;
import model.expressions.IExp;
import model.types.IntType;
import model.types.StringType;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class readFile implements IStmt{
    private IExp exp;
    private String varName;

    public readFile(IExp exp, String varName){
        this.exp = exp;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IMyDictionary<String, IValue> symbolTable = state.getSymTable();
        IMyDictionary<StringValue, BufferedReader> fileTable = state.getFiletable();

        if (!symbolTable.isDefined(varName)) {
            throw new MyException(String.format("Read File Error: Variable %s was not declared before.", varName));
        }

        IValue value = symbolTable.getValue(varName);
        if (!value.getType().equals(new IntType())) {
            throw new MyException(String.format("Read File Error: %s is not of type integer.", value));
        }

        value = exp.eval(symbolTable, state.getHeap());
        if (!value.getType().equals(new StringType())) {
            throw new MyException(String.format("Read File Error: %s is not of type string.", value));
        }

        StringValue castValue = (StringValue) value;
        if (!fileTable.isDefined(castValue)) {
            throw new MyException(String.format("Read File Error: No file %s found.", castValue));
        }

        BufferedReader bufferedReader = fileTable.getValue(castValue);
        try {
            String line = bufferedReader.readLine();
            if (line == null) {
                line = "0";
            }

            symbolTable.put(varName, new IntValue(Integer.parseInt(line)));
        } catch (IOException e) {
            throw new MyException(String.format("Read File Error: Could not read from file %s.", castValue));
        }

        return null;
    }

    @Override
    public IStmt deepCopy(){
        return new readFile(this.exp.deepCopy(),this.varName);
    }

    @Override
    public String toString(){
        return "readFile " + varName + ", " + exp.toString() + ")";
    }
}
