package template.primitve.generated.datastructure;

import org.junit.Test;

import java.util.PriorityQueue;

import static org.junit.Assert.*;

public class LongPriorityQueueTest {
    int limit = 10000;

    @Test
    public void testStd() {
        PriorityQueue<Long> pq = new PriorityQueue<>(limit);
        for (int i = limit - 1; i >= 0; i--) {
            pq.add((long) i);
        }
        while (!pq.isEmpty()) {
            pq.peek();
            pq.remove();
        }
    }

    @Test
    public void testCustom() {
        LongPriorityQueue pq = new LongPriorityQueue(limit, LongComparator.NATURE_ORDER);
        for (int i = limit - 1; i >= 0; i--) {
            pq.add(i);
        }
        while (!pq.isEmpty()) {
            pq.peek();
            pq.pop();
        }
    }
}