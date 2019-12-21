package template.primitve.generator;

public interface LongStack {
    void addLast(long x);

    long removeLast();

    long peekLast();

    LongIterator iterator();

    boolean isEmpty();
}
