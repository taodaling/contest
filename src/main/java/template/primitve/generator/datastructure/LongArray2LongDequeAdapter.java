package template.primitve.datastructure;

import template.primitve.generated.LongDeque;
import template.primitve.generated.LongIterator;

public class LongArray2LongDequeAdapter implements LongDeque {
    long[] data;
    int l;
    int r;



    public LongArray2LongDequeAdapter(long[] data) {
        this(data, 0, data.length - 1);
    }

    public LongArray2LongDequeAdapter(long[] data, int l, int r) {
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
    public long peekFirst() {
        return data[l];
    }

    @Override
    public void addFirst(long x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long removeFirst() {
        return data[l++];
    }

    @Override
    public long peekLast() {
        return data[r];
    }

    @Override
    public void addLast(long x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long removeLast() {
        return data[r--];
    }

    @Override
    public template.primitve.generated.LongIterator iterator() {
        return new LongIterator() {
            int iter = l;

            @Override
            public boolean hasNext() {
                return iter <= r;
            }

            @Override
            public long next() {
                return data[iter++];
            }
        };
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
