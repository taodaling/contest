package template.primitve.generated.datastructure;


public class MinDoubleQueue {
    DoubleDequeImpl minQueue;
    DoubleDequeImpl data;
    DoubleComparator comparator;

    public MinDoubleQueue(int cap, DoubleComparator comparator) {
        minQueue = new DoubleDequeImpl(cap);
        data = new DoubleDequeImpl(cap);
        this.comparator = comparator;
    }

    public void reset() {
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

    public double removeFirst() {
        double val = data.removeFirst();
        if (minQueue.peekFirst() == val) {
            minQueue.removeFirst();
        }
        return val;
    }

    public double peek() {
        return data.peekFirst();
    }

    public int size() {
        return data.size();
    }

    public double min() {
        return minQueue.peekFirst();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }
}
