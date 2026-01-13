package model.adt;

import java.util.List;

public interface IMyList<V>{
    void add(V value);
    List<V> getAll();
}
