package template.math;

import java.util.*;
import java.util.function.BiFunction;

public class Group<E> implements Iterable<E> {
    BiFunction<E, E, E> add;
    E identity;
    Set<E> set = new HashSet<>();
    List<E> added = new ArrayList<>();
    Deque<E> cand = new ArrayDeque<>();
    boolean commutative;

    public Group(boolean commutative, E identity, BiFunction<E, E, E> add) {
        this.add = add;
        this.identity = identity;
        this.commutative = commutative;
        add(identity);
    }

    public void add(E v) {
        if (set.add(v)) {
            cand.add(v);
        }
        consume();
    }

    private void consume() {
        while (!cand.isEmpty()) {
            E first = cand.removeFirst();
            added.add(first);
            for (E x : added) {
                E y = add.apply(x, first);
                if (set.add(y)) {
                    cand.add(y);
                }
                if (!commutative) {
                    E z = add.apply(first, x);
                    if (set.add(z)) {
                        cand.add(z);
                    }
                }
            }
        }
    }

    @Override
    public Iterator<E> iterator() {
        return added.iterator();
    }

    public int size() {
        return added.size();
    }


    @Override
    public String toString() {
        return added.toString();
    }
}
