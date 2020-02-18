package template.primitve.generated.datastructure;

public class DoubleMinStack {
    DoubleDequeImpl minDeque;
    DoubleDequeImpl data;
    DoubleComparator comparator;

    public DoubleMinStack(int cap, DoubleComparator comparator) {
        minDeque = new DoubleDequeImpl(cap);
        data = new DoubleDequeImpl(cap);
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

    public double peek() {
        return data.peekLast();
    }

    public double pop() {
        double val = data.removeLast();
        if (minDeque.peekLast() == val) {
            minDeque.removeLast();
        }
        return val;
    }
}
