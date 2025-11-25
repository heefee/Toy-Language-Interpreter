package model.adt;

public interface IMyStack<T>{
    void push(T elem);
    T pop();
    boolean isEmpty();

}
