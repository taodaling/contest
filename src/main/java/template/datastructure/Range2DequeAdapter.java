package template.datastructure;

import java.util.Iterator;
import java.util.function.IntFunction;

public class Range2DequeAdapter<T> implements SimplifiedDeque<T> {
    IntFunction<T> data;
    int l;
    int r;

    public Range2DequeAdapter(IntFunction<T> data, int l, int r) {
        this.data = data;
        this.l = l;
        this.r = r;
    }

    public boolean isEmpty() {
        return l > r;
    }

    public int size() {
        return r - l + 1;
    }

    @Override
    public T peekFirst() {
        return data.apply(l);
    }

    @Override
    public void addFirst(T x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T removeFirst() {
        return data.apply(l++);
    }

    @Override
    public T peekLast() {
        return data.apply(r);
    }

    @Override
    public void addLast(T x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T removeLast() {
        return data.apply(r--);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int iter = l;

            @Override
            public boolean hasNext() {
                return iter <= r;
            }

            @Override
            public T next() {
                return data.apply(iter++);
            }
        };
    }
}
