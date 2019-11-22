package template.datastructure;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;

public class IntDequeBetaTest {
    @Test
    public void test1() {
        IntDequeBeta deque = new IntDequeBeta(0);
        deque.addLast(1);
        deque.addFirst(2);
        deque.addLast(3);

        Assert.assertEquals(3, deque.size());
        Assert.assertFalse(deque.isEmpty());
        IntIterator iterator = deque.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(3, iterator.next());

        Assert.assertEquals(2, deque.peekFirst());
        Assert.assertEquals(3, deque.peekLast());
        Assert.assertEquals(2, deque.removeFirst());
        Assert.assertEquals(3, deque.removeLast());
        Assert.assertEquals(1, deque.removeLast());
    }

    @Test
    public void test2() {
        int n = 10000000;
        Deque<Integer> deque = new ArrayDeque<>(n);
        for (int i = 0; i < n; i++) {
            deque.addLast(i);
        }
        while (!deque.isEmpty()) {
            deque.peekLast();
            deque.peekFirst();
            deque.removeFirst();
            if (!deque.isEmpty()) {
                deque.removeLast();
            }
        }
    }

    @Test
    public void test3() {
        int n = 10000000;
        IntDeque deque = new IntDeque(n);
        for (int i = 0; i < n; i++) {
            deque.addLast(i);
        }
        while (!deque.isEmpty()) {
            deque.peekLast();
            deque.peekFirst();
            deque.removeFirst();
            if (!deque.isEmpty()) {
                deque.removeLast();
            }
        }
    }

    @Test
    public void test4() {
        int n = 10000000;
        IntDequeBeta deque = new IntDequeBeta(n);
        for (int i = 0; i < n; i++) {
            deque.addLast(i);
        }
        while (!deque.isEmpty()) {
            deque.peekLast();
            deque.peekFirst();
            deque.removeFirst();
            if (!deque.isEmpty()) {
                deque.removeLast();
            }
        }
    }

    @Test
    public void test5() {
        int n = 10000000;
        MultiWayIntDeque deque = new MultiWayIntDeque(1, n);
        for (int i = 0; i < n; i++) {
            deque.addLast(0, i);
        }
        while (!deque.isEmpty(0)) {
            deque.peekLast(0);
            deque.peekFirst(0);
            deque.removeFirst(0);
            if (!deque.isEmpty(0)) {
                deque.removeLast(0);
            }
        }
    }
}