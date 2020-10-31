package template.primitve.generated.datastructure;


public class LongMinQueue {
    LongDequeImpl minQueue;
    LongDequeImpl data;
    LongComparator comparator;

    public LongMinQueue(int cap, LongComparator comparator) {
        minQueue = new LongDequeImpl(cap);
        data = new LongDequeImpl(cap);
        this.comparator = comparator;
    }

    public void reset() {
        minQueue.clear();
        data.clear();
    }

    public void addLast(long val) {
        data.addLast(val);
        while (!minQueue.isEmpty() && comparator.compare(minQueue.peekLast(), val) > 0) {
            minQueue.removeLast();
        }
        minQueue.addLast(val);
    }

    public long removeFirst() {
        assert size() > 0;
        long val = data.removeFirst();
        if (minQueue.peekFirst() == val) {
            minQueue.removeFirst();
        }
        return val;
    }

    public long peek() {
        assert size() > 0;
        return data.peekFirst();
    }

    public int size() {
        return data.size();
    }

    public long min() {
        assert size() > 0;
        return minQueue.peekFirst();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }
}
