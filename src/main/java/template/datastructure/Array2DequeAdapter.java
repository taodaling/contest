package template.datastructure;

import java.util.Iterator;

public class Array2DequeAdapter<T> implements SimplifiedDeque<T> {
    T[] data;
    int l;
    int r;

    public Array2DequeAdapter(T[] data) {
        this(data, 0, data.length - 1);
    }

    public Array2DequeAdapter(T[] data, int l, int r) {
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
        return data[l];
    }

    @Override
    public void addFirst(T x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T removeFirst() {
        return data[l++];
    }

    @Override
    public T peekLast() {
        return data[r];
    }

    @Override
    public void addLast(T x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T removeLast() {
        return data[r--];
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
                return data[iter++];
            }
        };
    }
}
