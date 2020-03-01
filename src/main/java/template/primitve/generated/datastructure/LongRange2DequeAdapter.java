package template.primitve.generated.datastructure;


public class LongRange2DequeAdapter implements LongDeque {
    IntToLongFunction function;
    int l;
    int r;


    public LongRange2DequeAdapter(IntToLongFunction function, int l, int r) {
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
    public long peekFirst() {
        return function.apply(l);
    }

    @Override
    public void addFirst(long x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long removeFirst() {
        return function.apply(l++);
    }

    @Override
    public long peekLast() {
        return function.apply(r);
    }

    @Override
    public void addLast(long x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long removeLast() {
        return function.apply(r--);
    }

    @Override
    public LongIterator iterator() {
        return new LongIterator() {
            int iter = l;

            @Override
            public boolean hasNext() {
                return iter <= r;
            }

            @Override
            public long next() {
                return function.apply(iter++);
            }
        };
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
