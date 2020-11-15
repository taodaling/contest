package template.primitve.generated.datastructure;


public class DoubleMinQueue {
    DoubleDequeImpl minQueue;
    DoubleDequeImpl data;
    DoubleComparator comparator;

    public DoubleMinQueue(int cap, DoubleComparator comparator) {
        minQueue = new DoubleDequeImpl(cap);
        data = new DoubleDequeImpl(cap);
        this.comparator = comparator;
    }

    public void clear() {
        minQueue.clear();
        data.clear();
    }

    public void addLast(double val) {
        data.addLast(val);
        while (!minQueue.isEmpty() && comparator.compare(minQueue.peekLast(), val) > 0) {
            minQueue.removeLast();
        }
        minQueue.addLast(val);
    }

    public double removeFirst() {
        assert size() > 0;
        double val = data.removeFirst();
        if (minQueue.peekFirst() == val) {
            minQueue.removeFirst();
        }
        return val;
    }

    public double peek() {
        assert size() > 0;
        return data.peekFirst();
    }

    public int size() {
        return data.size();
    }

    public double min() {
        assert size() > 0;
        return minQueue.peekFirst();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }
}
