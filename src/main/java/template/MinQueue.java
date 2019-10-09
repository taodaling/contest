package template;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

/**
 * Created by dalt on 2018/5/20.
 */
public class MinQueue<T> {
    Deque<T> data;
    Deque<T> increasing;
    Comparator<T> comparator;

    public MinQueue(int cap, Comparator<T> comparator) {
        data = new ArrayDeque<>(cap);
        increasing = new ArrayDeque<>(cap);
        this.comparator = comparator;
    }

    public void enqueue(T x) {
        while (!increasing.isEmpty() && comparator.compare(x, increasing.peekLast()) < 0) {
            increasing.removeLast();
        }
        increasing.addLast(x);
        data.addLast(x);
    }

    public T deque() {
        T head = data.removeFirst();
        if (increasing.peekFirst() == head) {
            increasing.removeFirst();
        }
        return head;
    }

    public void clear() {
        data.clear();
        increasing.clear();
    }

    public T query() {
        return increasing.peekFirst();
    }
}
