package template.primitve.generated.datastructure;

public interface IntegerDeque extends IntegerStack {
    void addFirst(int x);
    int removeFirst();
    int peekFirst();
    IntegerIterator iterator();
}