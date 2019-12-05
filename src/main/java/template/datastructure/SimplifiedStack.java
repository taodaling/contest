package template.datastructure;

public interface SimplifiedStack<T> extends Iterable<T>{
    boolean isEmpty();

    T peekLast();

    void addLast(T x);

    T removeLast();
}
