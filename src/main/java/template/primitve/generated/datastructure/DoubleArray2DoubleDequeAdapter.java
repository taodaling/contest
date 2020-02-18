package template.primitve.generated.datastructure;

import template.primitve.generated.datastructure.DoubleDeque;
import template.primitve.generated.datastructure.DoubleIterator;

public class DoubleArray2DoubleDequeAdapter implements DoubleDeque {
    double[] data;
    int l;
    int r;



    public DoubleArray2DoubleDequeAdapter(double[] data) {
        this(data, 0, data.length - 1);
    }

    public DoubleArray2DoubleDequeAdapter(double[] data, int l, int r) {
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
    public double peekFirst() {
        return data[l];
    }

    @Override
    public void addFirst(double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double removeFirst() {
        return data[l++];
    }

    @Override
    public double peekLast() {
        return data[r];
    }

    @Override
    public void addLast(double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double removeLast() {
        return data[r--];
    }

    @Override
    public DoubleIterator iterator() {
        return new DoubleIterator() {
            int iter = l;

            @Override
            public boolean hasNext() {
                return iter <= r;
            }

            @Override
            public double next() {
                return data[iter++];
            }
        };
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
