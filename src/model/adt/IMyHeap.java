package model.adt;

import exceptions.MyException;
import model.values.IValue;

import java.util.HashMap;
import java.util.Map;

public interface IMyHeap<K,V> {
    int getFreeValue();

    Map<K, V> getContent();

    void setContent(Map<K, V> newMap);

    int add(V value);

    void update(K position, V value) throws MyException;

    IValue get(K position) throws MyException;

    K getLastKey();
    K getNextKey();
}
