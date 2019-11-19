package template;

import org.junit.Assert;
import org.junit.Test;

public class MultiWayIntDequeTest {

    @Test
    public void test1(){
        MultiWayIntDeque deque = new MultiWayIntDeque(3, 0);

        deque.addFirst(0, 1);
        deque.addFirst(0, 3);
        deque.addLast(0, 2);

        deque.addFirst(1, 2);
        deque.addFirst(1, 1);
        deque.addLast(1, 3);

        deque.addFirst(2, 3);
        deque.addFirst(2, 1);
        deque.addLast(2, 2);

        Assert.assertEquals(3, deque.removeFirst(0));
        Assert.assertEquals(1, deque.removeFirst(0));
        Assert.assertEquals(2, deque.removeFirst(0));

        Assert.assertEquals(3, deque.removeLast(1));
        Assert.assertEquals(2, deque.removeLast(1));
        Assert.assertEquals(1, deque.removeLast(1));

        Assert.assertEquals(1, deque.removeFirst(2));
        Assert.assertEquals(3, deque.removeFirst(2));
        Assert.assertEquals(2, deque.peekFirst(2));
        Assert.assertEquals(2, deque.peekLast(2));
    }
}