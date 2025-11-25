package model.adt;
import java.util.Stack;

public class MyStack <T> implements IMyStack<T> {
    private Stack<T> tail;
    public MyStack(){
        this.tail = new Stack<>();
    }

    @Override
    public void push(T elem) {
        this.tail.push(elem);
    }

    @Override
    public T pop() {
        return this.tail.pop();
    }

    @Override
    public boolean isEmpty() {
        return this.tail.isEmpty();
    }

    @Override
    public String toString() {
        if (tail.isEmpty()) return "Execution Stack is empty! \n";
        Stack<T> copyTail = new Stack<>();
        copyTail.addAll(tail);
        StringBuilder result = new StringBuilder();
        result.append("Execution Stack: \n    ");
        for (T elem : copyTail.reversed()) {
            result.append(elem).append(" \n    ");
        }
        return result.toString();
     }
}
