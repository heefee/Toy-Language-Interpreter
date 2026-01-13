package model.adt;

import java.util.List;

public interface IMyStack<T>{
    void push(T elem);
    T pop();
    boolean isEmpty();
    List <T> toList();
}
