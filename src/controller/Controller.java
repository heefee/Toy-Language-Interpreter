package controller;

import exceptions.MyException;
import model.PrgState;
import model.adt.IMyHeap;
//import model.adt.IMyList;
//import model.adt.IMyStack;
//import model.adt.MyHeap;
//import model.statements.IStmt;
import model.values.IValue;
import model.values.RefValue;
import repository.IRepository;
//import repository.Repository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

//import static java.lang.IO.print;

public class Controller {
    private final IRepository statesRepo;
    private ExecutorService executor;

    public Controller(IRepository statesRepo) {
        this.statesRepo = statesRepo;
    }

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList){
        return inPrgList.stream()
                .filter(p -> p.isNotCompleted())
                .collect(Collectors.toList());
    }

    private void logPrg(List<PrgState> prgList){
        try {
            prgList.stream().forEach(prg -> {
                try {
                    statesRepo.logPrgStateExec(prg);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        } catch (RuntimeException ex) {
            throw new MyException(ex.getMessage());
        }
    }

    public void oneStepForAllPrg(List<PrgState> prgList) throws MyException {
        logPrg(prgList);

        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) (() -> p.oneStep()))
                .collect(Collectors.toList());
        try {
            List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }

                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            prgList.addAll(newPrgList);
        } catch(InterruptedException | RuntimeException exception) {
            throw new MyException(exception.getMessage());
        }
        logPrg(prgList);
        statesRepo.setPrgList(prgList);
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

    Set<Integer> getAddrFromHeap(IMyHeap<Integer,IValue> heap){
        Set <Integer> rez = new HashSet<>();
        heap.getContent().values().stream()
                .filter(v -> v instanceof RefValue)
                .forEach(v->{
        while (v instanceof RefValue) {
            RefValue v1 = (RefValue) v;
            rez.add(v1.getAddress());
            v = heap.getContent().get(v1.getAddress());
            /// modificat
        }});

        return rez;
    }

    Map<Integer, IValue> safeGarbageCollector(Set<Integer> symTableAddr, Set<Integer> heapAddr ,HashMap<Integer,IValue> heap){
        return heap.entrySet()
                .stream()
                .filter(e->symTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public void allStep() throws MyException, IOException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(statesRepo.getPrgList());
        while(!prgList.isEmpty()) {
            Set <Integer> SymTblAddr = new HashSet<>();
            for (PrgState prg : statesRepo.getPrgList()) {
                SymTblAddr.addAll(getAddrFromSymTable(prg.getSymTable().getContent().values()));
            }
            prgList.forEach(prgState -> {
                IMyHeap<Integer, IValue> heap = prgState.getHeap();
                Map<Integer, IValue> newHeapContent = safeGarbageCollector(
                        SymTblAddr,
                        getAddrFromHeap(heap),
                        (HashMap<Integer, IValue>) heap.getContent()
                );

                heap.setContent(newHeapContent);
            });
            oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(statesRepo.getPrgList());
        }
        executor.shutdownNow();
        statesRepo.setPrgList(prgList);
        statesRepo.logFinalLine();
    }

}
