package template.primitve.generated.datastructure;


public class IntegerMinQueue {
    IntegerDequeImpl minQueue;
    IntegerDequeImpl data;
    IntegerComparator comparator;

    public IntegerMinQueue(int cap, IntegerComparator comparator) {
        minQueue = new IntegerDequeImpl(cap);
        data = new IntegerDequeImpl(cap);
        this.comparator = comparator;
    }

    public void clear() {
        minQueue.clear();
        data.clear();
    }

    public void addLast(int val) {
        data.addLast(val);
        while (!minQueue.isEmpty() && comparator.compare(minQueue.peekLast(), val) > 0) {
            minQueue.removeLast();
        }
        minQueue.addLast(val);
    }

    public int removeFirst() {
        assert size() > 0;
        int val = data.removeFirst();
        if (minQueue.peekFirst() == val) {
            minQueue.removeFirst();
        }
        return val;
    }

    public int peek() {
        assert size() > 0;
        return data.peekFirst();
    }

    public int size() {
        return data.size();
    }

    public int min() {
        assert size() > 0;
        return minQueue.peekFirst();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }
}
