package template.primitve.generated;

public interface IntegerStack {
    void addLast(int x);

    int removeLast();

    int peekLast();

    IntegerIterator iterator();

    boolean isEmpty();
}
