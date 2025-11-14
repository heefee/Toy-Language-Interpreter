package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.types.BoolType;
import model.types.IType;
import model.types.IntType;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;

public class VarDeclStmt implements IStmt{
    private String id;
    private IType type;

    public VarDeclStmt(String newId, IType newType) {
        this.id = newId;
        this.type = newType;
    }

    @Override
    public String toString() {
        return this.type.toString() + " " + this.id;
    }

    @Override
    public IStmt deepCopy() {
        return new VarDeclStmt(this.id,this.type.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable();
        if(!symTable.isDefined(this.id)) {
            if (this.type.equals(new IntType())) {
                symTable.put(this.id, new IntValue(0));
            }
            else if (this.type.equals(new BoolType())){
                symTable.put(this.id, new BoolValue(false));
            }
            else{
                symTable.put(this.id, new StringValue(""));
            }
        }
        else {
            throw new MyException("This variable is already defined");
        }
        return state;
    }
}
