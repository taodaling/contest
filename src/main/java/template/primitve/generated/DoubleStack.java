package template.primitve.generated;

public interface DoubleStack {
    void addLast(double x);

    double removeLast();

    double peekLast();

    DoubleIterator iterator();

    boolean isEmpty();
}
