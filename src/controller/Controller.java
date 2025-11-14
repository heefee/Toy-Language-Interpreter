package controller;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIStack;
import model.statements.IStmt;
import repository.IRepository;

import java.io.IOException;

public class Controller {
    private IRepository statesRepo;

    public Controller(IRepository statesRepo) {
        this.statesRepo = statesRepo;
    }


    public PrgState oneStep(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getExeStack();
        if (stk.isEmpty()) throw new MyException("prgstate stack is empty");
        IStmt crtStmt = stk.pop();
        return crtStmt.execute(state);
    }

    public void allStep() throws MyException, IOException {
        PrgState prg = statesRepo.getCrtPrg();
        statesRepo.logPrgStateExec();
        while (!prg.getExeStack().isEmpty()) {
            oneStep(prg);
            statesRepo.logPrgStateExec();
        }
        statesRepo.logFinalLine();
    }

}
