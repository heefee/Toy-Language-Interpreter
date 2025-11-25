package model.adt;

import model.values.StringValue;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K,V> implements IMyDictionary<K,V> {
    private Map<K, V> dict;

    public MyDictionary(){
        this.dict = new HashMap<>();
    }
    @Override
    public void put(K key, V value) {
        dict.put(key,value);
    }

    @Override
    public boolean isDefined(K key) {
        return dict.containsKey(key);
    }

    @Override
    public V getValue(K key) {
        return dict.get(key);
    }

    @Override
    public String toString(){
        return dict.toString();
    }

    @Override
    public void remove(K id) {
        dict.remove(id);
    }

    @Override
    public Map<K, V> getContent(){
        return (Map<K, V>) dict;
    }
}
