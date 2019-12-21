package template.primitve.generated;

public interface LongStack {
    void addLast(long x);

    long removeLast();

    long peekLast();

    LongIterator iterator();

    boolean isEmpty();
}
