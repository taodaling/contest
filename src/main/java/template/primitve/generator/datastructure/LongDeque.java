package template.primitve.datastructure;

import template.primitve.generated.LongIterator;
import template.primitve.generated.LongStack;

public interface LongDeque extends LongStack {
    void addFirst(long x);
    long removeFirst();
    long peekFirst();
    LongIterator iterator();
    void clear();
}