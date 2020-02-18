package template.primitve.generated.datastructure;

public class LongMinStack {
    LongDequeImpl minDeque;
    LongDequeImpl data;
    LongComparator comparator;

    public LongMinStack(int cap, LongComparator comparator) {
        minDeque = new LongDequeImpl(cap);
        data = new LongDequeImpl(cap);
        this.comparator = comparator;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void push(int val) {
        if (comparator.compare(minDeque.peekLast(), val) >= 0) {
            minDeque.addLast(val);
        }
        data.addLast(val);
    }

    public long peek() {
        return data.peekLast();
    }

    public long pop() {
        long val = data.removeLast();
        if (minDeque.peekLast() == val) {
            minDeque.removeLast();
        }
        return val;
    }
}
