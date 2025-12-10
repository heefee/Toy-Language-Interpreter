package repository;

import exceptions.MyException;
import model.PrgState;

import java.io.IOException;
import java.util.List;

public interface IRepository {
    void addPrgState(PrgState newState);
    //PrgState getCrtPrg();
    List<PrgState> getPrgList();
    void logPrgStateExec(PrgState prg) throws IOException;
    void logFinalLine() throws IOException;
    void setPrgList(List<PrgState> givenPrg);
}
