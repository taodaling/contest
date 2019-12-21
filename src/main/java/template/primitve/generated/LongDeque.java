package template.primitve.generated;

public interface LongDeque extends LongStack{
    void addFirst(long x);
    long removeFirst();
    long peekFirst();
    LongIterator iterator();
}