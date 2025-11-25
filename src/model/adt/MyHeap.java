package model.adt;

import exceptions.MyException;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.valueOf;

public class MyHeap implements IMyHeap<Integer,IValue> {
    private HashMap<Integer,IValue> heapTable;
    private int freeVal;

    private static int lastKey = 0;

    public java.lang.Integer getNextKey(){
        return ++lastKey;
    }

    public Integer getLastKey(){
        return lastKey;
    }

    public Integer newFreeValue(){
        freeVal += 1;
        if(heapTable.containsKey(freeVal)){
            freeVal+=1;
        }
        return freeVal;
    }

    public MyHeap(){
        heapTable = new HashMap<>();
        freeVal = 1;
    }

    @Override
    public int getFreeValue() {
        return freeVal;
    }

    @Override
    public Map<Integer, IValue> getContent() {
        return heapTable;
    }

    @Override
    public void setContent(Map<Integer, IValue> newMap) {
        heapTable.clear();
        for (Integer i : newMap.keySet()) {
            heapTable.put(i, newMap.get(i));
        }
    }

    @Override
    public int add(IValue val) {
        heapTable.put(getNextKey(),val);
        freeVal = newFreeValue();
        return freeVal;
    }

    @Override
    public void update(Integer position, IValue val) throws MyException {
        if (!heapTable.containsKey(position)){
            throw new MyException("Heap error, the position is not on the heap");
        }
        heapTable.put(position,val);
    }

    @Override
    public IValue get(Integer position) throws MyException {
        if (!heapTable.containsKey(position)){
            throw new MyException("Heap error, the position is not on the heap");
        }
        return heapTable.get(position);
    }

    @Override
    public String toString(){
        return heapTable.toString();
    }
}
