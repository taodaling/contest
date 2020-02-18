package template.primitve.generated.datastructure;


public interface LongStack {
    void addLast(long x);

    long removeLast();

    long peekLast();

    LongIterator iterator();

    boolean isEmpty();

    void clear();
}
