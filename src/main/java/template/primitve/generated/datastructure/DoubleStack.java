package template.primitve.generated.datastructure;

import template.primitve.generated.datastructure.DoubleIterator;

public interface DoubleStack {
    void addLast(double x);

    double removeLast();

    double peekLast();

    DoubleIterator iterator();

    boolean isEmpty();
}
