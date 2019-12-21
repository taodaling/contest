package template.primitve.generated;

public interface IntegerDeque extends IntegerStack{
    void addFirst(int x);
    int removeFirst();
    int peekFirst();
    IntegerIterator iterator();
}