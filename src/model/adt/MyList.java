package model.adt;

import java.util.ArrayList;
import java.util.List;

public class MyList<V> implements MyIList<V>{
    private List<V> list;

    public MyList(){
        list = new ArrayList<>();
    }

    @Override
    public void add(V value) {
        list.add(value);
    }

    @Override
    public String toString(){
        return list.toString();
    }
}
