package model.adt;

import model.types.IType;
import model.values.StringValue;

import java.io.BufferedReader;
import java.util.Map;

public interface IMyDictionary<K,V> {
    void put(K key,V value);
    boolean isDefined(K key);
    V getValue(K key);
    void remove(K key);
    Map<K, V> getContent();
    IMyDictionary<K,V> deepCopy();
    V getType(K key);
}
