package template.primitve.generated.datastructure;

public interface DoubleDeque extends DoubleStack {
    void addFirst(double x);
    double removeFirst();
    double peekFirst();
    DoubleIterator iterator();
    DoubleIterator reverseIterator();
}