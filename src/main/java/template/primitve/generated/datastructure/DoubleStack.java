package template.primitve.generated.datastructure;


public interface DoubleStack {
    void addLast(double x);

    double removeLast();

    double peekLast();

    DoubleIterator iterator();

    boolean isEmpty();

    void clear();
}
