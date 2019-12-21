package template.primitve.generator;

public interface LongDeque extends LongStack{
    void addFirst(long x);
    long removeFirst();
    long peekFirst();
    LongIterator iterator();
}