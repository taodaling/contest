package template.datastructure;

public interface SimplifiedDeque<T> extends SimplifiedStack<T> {
    T peekFirst();

    void addFirst(T x);

    T removeFirst();
}
