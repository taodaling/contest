package template.primitve.datastructure;

import template.primitve.generated.LongIterator;

public interface LongStack {
    void addLast(long x);

    long removeLast();

    long peekLast();

    LongIterator iterator();

    boolean isEmpty();
}
