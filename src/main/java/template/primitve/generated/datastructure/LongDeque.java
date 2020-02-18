package template.primitve.generated.datastructure;

public interface LongDeque extends LongStack {
    void addFirst(long x);
    long removeFirst();
    long peekFirst();
    LongIterator iterator();
}