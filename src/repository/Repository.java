package repository;

import model.PrgState;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class Repository implements IRepository {
    private final List<PrgState> prgStates;
    private String logFilePath;



    public Repository(PrgState initialPrg, String logFilePath) throws IOException {
        this.prgStates = new ArrayList<>();
        this.prgStates.add(initialPrg);
        this.logFilePath = logFilePath;
    }

    @Override
    public void addPrgState(PrgState state) {
        this.prgStates.add(state);
    }

    @Override
    public PrgState getCrtPrg() {
        if(this.prgStates.isEmpty()) {
            return null;
        }
        return this.prgStates.get(0);
    }

    @Override
    public List<PrgState> getPrgList() {
        return this.prgStates;
    }

    public int getSize(){
        return this.prgStates.size();
    }

    @Override
    public void logPrgStateExec() throws IOException {
        PrintWriter logFile;
        logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath,true)));
        logFile.println(getCrtPrg().toString());
        logFile.close();
    }

    public void logFinalLine() throws IOException{
        PrintWriter logFile;
        logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath,true)));
        logFile.println("--------------------------------------------");
        logFile.close();
    }
}