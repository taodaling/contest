package template.primitve.generated.datastructure;


public class IntegerRange2DequeAdapter implements IntegerDeque {
    IntToIntegerFunction function;
    int l;
    int r;


    public IntegerRange2DequeAdapter(IntToIntegerFunction function, int l, int r) {
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
    public int peekFirst() {
        return function.apply(l);
    }

    @Override
    public void addFirst(int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int removeFirst() {
        return function.apply(l++);
    }

    @Override
    public int peekLast() {
        return function.apply(r);
    }

    @Override
    public void addLast(int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int removeLast() {
        return function.apply(r--);
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
                return function.apply(iter++);
            }
        };
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
