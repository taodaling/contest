package template.primitve.generated;

public interface DoubleDeque extends DoubleStack{
    void addFirst(double x);
    double removeFirst();
    double peekFirst();
    DoubleIterator iterator();
}