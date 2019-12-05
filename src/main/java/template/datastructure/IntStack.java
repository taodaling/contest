package template.datastructure;

public interface IntStack {
    void addLast(int x);

    int removeLast();

    int peekLast();

    IntIterator iterator();

    boolean isEmpty();
}
