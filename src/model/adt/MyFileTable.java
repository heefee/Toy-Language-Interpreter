package model.adt;

import model.types.IType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class MyFileTable<K,V> implements IMyDictionary<K,V> {
    private Map<K, V> fileTable;


    public MyFileTable(){
        fileTable = new HashMap<>();
    }
    @Override
    public void put(K key, V value) {
        fileTable.put(key,value);
    }

    @Override
    public boolean isDefined(K key) {
        return fileTable.containsKey(key);
    }

    @Override
    public V getValue(K key) {
        return fileTable.get(key);
    }

    @Override
    public String toString(){
        return fileTable.toString();
    }

    @Override
    public void remove(K id) {
        fileTable.remove(id);
    }

    @Override
    public Map<K,V> getContent(){
        return (Map<K, V>) fileTable;
    }

    @Override
    public IMyDictionary<K,V> deepCopy() {
        IMyDictionary<K,V> newDict = new MyDictionary<>();

        for(K key : fileTable.keySet()) {
            newDict.put(key, fileTable.get(key));
        }
        return newDict;
    }


    public V getType(K key) {
        return fileTable.get(key);
    }
}
