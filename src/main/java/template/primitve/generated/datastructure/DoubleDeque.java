package template.primitve.generated.datastructure;

import template.primitve.generated.datastructure.DoubleIterator;
import template.primitve.generated.datastructure.DoubleStack;

public interface DoubleDeque extends DoubleStack {
    void addFirst(double x);
    double removeFirst();
    double peekFirst();
    DoubleIterator iterator();
    void clear();
}