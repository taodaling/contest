package template.datastructure;

import template.utils.IntComparator;

public class MinIntStack {
    IntDequeBeta minDeque;
    IntDequeBeta data;
    IntComparator comparator;

    public MinIntStack(int cap, IntComparator comparator) {
        minDeque = new IntDequeBeta(cap);
        data = new IntDequeBeta(cap);
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

    public int peek() {
        return data.peekLast();
    }

    public int pop() {
        int val = data.removeLast();
        if (minDeque.peekLast() == val) {
            minDeque.removeLast();
        }
        return val;
    }
}
