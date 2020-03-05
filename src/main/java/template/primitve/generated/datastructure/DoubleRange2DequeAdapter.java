package template.primitve.generated.datastructure;


public class DoubleRange2DequeAdapter implements DoubleDeque {
    IntToDoubleFunction function;
    int l;
    int r;

    public DoubleRange2DequeAdapter(IntToDoubleFunction function, int l, int r) {
        this.function = function;
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
        return function.apply(l);
    }

    @Override
    public void addFirst(double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double removeFirst() {
        return function.apply(l++);
    }

    @Override
    public double peekLast() {
        return function.apply(r);
    }

    @Override
    public void addLast(double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double removeLast() {
        return function.apply(r--);
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
                return function.apply(iter++);
            }
        };
    }

    @Override
    public DoubleIterator reverseIterator() {
        return new DoubleIterator() {
            int iter = r;

            @Override
            public boolean hasNext() {
                return iter >= l;
            }

            @Override
            public double next() {
                return function.apply(iter--);
            }
        };
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
