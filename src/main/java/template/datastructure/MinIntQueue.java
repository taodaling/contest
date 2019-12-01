package template.datastructure;

import template.utils.IntComparator;

public class MinIntQueue {
    IntDequeBeta minQueue;
    IntDequeBeta data;
    IntComparator comparator;

    public MinIntQueue(int cap, IntComparator comparator) {
        minQueue = new IntDequeBeta(cap);
        data = new IntDequeBeta(cap);
        this.comparator = comparator;
    }

    public void reset() {
        minQueue.clear();
        data.clear();
    }

    public void enqueue(int val) {
        data.addLast(val);
        while (!minQueue.isEmpty() && comparator.compare(minQueue.peekLast(), val) > 0) {
            minQueue.removeLast();
        }
        minQueue.addLast(val);
    }

    public int deque() {
        int val = data.removeFirst();
        if (minQueue.peekFirst() == val) {
            minQueue.removeFirst();
        }
        return val;
    }

    public int peek() {
        return data.peekFirst();
    }

    public int size() {
        return data.size();
    }

    public int min() {
        return minQueue.peekFirst();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }
}
