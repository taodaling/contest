package template.primitve.generated.datastructure;

import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerIterator;

public class IntegerArray2IntegerDequeAdapter implements IntegerDeque {
    int[] data;
    int l;
    int r;



    public IntegerArray2IntegerDequeAdapter(int[] data) {
        this(data, 0, data.length - 1);
    }

    public IntegerArray2IntegerDequeAdapter(int[] data, int l, int r) {
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
    public int peekFirst() {
        return data[l];
    }

    @Override
    public void addFirst(int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int removeFirst() {
        return data[l++];
    }

    @Override
    public int peekLast() {
        return data[r];
    }

    @Override
    public void addLast(int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int removeLast() {
        return data[r--];
    }

    @Override
    public IntegerIterator iterator() {
        return new IntegerIterator() {
            int iter = l;

            @Override
            public boolean hasNext() {
                return iter <= r;
            }

            @Override
            public int next() {
                return data[iter++];
            }
        };
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
