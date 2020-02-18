package template.primitve.generated.datastructure;

import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerStack;

public interface IntegerDeque extends IntegerStack {
    void addFirst(int x);
    int removeFirst();
    int peekFirst();
    IntegerIterator iterator();
    void clear();
}