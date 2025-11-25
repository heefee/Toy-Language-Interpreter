package controller;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyHeap;
import model.adt.IMyList;
import model.adt.IMyStack;
import model.statements.IStmt;
import model.values.IValue;
import model.values.RefValue;
import repository.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.IO.print;

public class Controller {
    private IRepository statesRepo;

    public Controller(IRepository statesRepo) {
        this.statesRepo = statesRepo;
    }


    Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddr, HashMap<Integer,IValue> heap){
        return heap.entrySet().stream()
                .filter(e->symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    Set<Integer> getAddrFromSymTable(Collection<IValue> symTableValues){
        return symTableValues.stream()
                .filter(v-> v instanceof RefValue)
                .map(v-> {
                    RefValue v1 = (RefValue) v; return v1.getAddress();})
                .collect(Collectors.toSet());
    }

    Set<Integer> getAddrFromHeap(Collection<IValue> heapValues){
        Set <Integer> rez = new HashSet<>();
        heapValues.stream()
                .filter(v -> v instanceof RefValue)
                .forEach(v->{
        while (v instanceof RefValue) {
            RefValue v1 = (RefValue) v;
            rez.add(v1.getAddress());
        }});

        return rez;
    }

    Map<Integer, IValue> safeGarbageCollector(Set<Integer> symTableAddr, Set<Integer> heapAddr ,HashMap<Integer,IValue> heap){
        return heap.entrySet()
                .stream()
                .filter(e->symTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public PrgState oneStep(PrgState state) throws MyException {
        IMyStack<IStmt> stk = state.getExeStack();
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
            prg.getHeap().setContent(safeGarbageCollector(
                    getAddrFromSymTable(
                            prg.getSymTable().getContent().values()), getAddrFromHeap(prg.getHeap().getContent().values()), (HashMap<Integer, IValue>) prg.getHeap().getContent()));
            statesRepo.logPrgStateExec();
        }
        statesRepo.logFinalLine();
    }

}
