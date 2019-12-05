package template.datastructure;

import template.utils.RevokeIterator;

import java.util.Arrays;
import java.util.Iterator;

public class MultiWayDeque<V> {
    private Object[] values;
    private int[] next;
    private int[] prev;
    private int[] heads;
    private int[] tails;
    private int alloc;
    private int queueNum;

    public RevokeIterator<V> iterator(final int queue) {
        return new RevokeIterator() {
            int ele = heads[queue];

            @Override
            public boolean hasNext() {
                return ele != 0;
            }

            @Override
            public Object next() {
                Object ans = values[ele];
                ele = next[ele];
                return ans;
            }

            @Override
            public void revoke() {
                ele = prev[ele];
            }
        };
    }

    public SimplifiedDeque<V> queue(int qId) {
        return new SimplifiedDeque<V>() {
            @Override
            public boolean isEmpty() {
                return MultiWayDeque.this.isEmpty(qId);
            }

            @Override
            public V peekFirst() {
                return MultiWayDeque.this.peekFirst(qId);
            }

            @Override
            public V peekLast() {
                return MultiWayDeque.this.peekLast(qId);
            }

            @Override
            public void addFirst(V x) {
                MultiWayDeque.this.addFirst(qId, x);
            }

            @Override
            public void addLast(V x) {
                MultiWayDeque.this.addLast(qId, x);
            }

            @Override
            public V removeFirst() {
                return MultiWayDeque.this.removeFirst(qId);
            }

            @Override
            public V removeLast() {
                return MultiWayDeque.this.removeLast(qId);
            }

            @Override
            public Iterator<V> iterator() {
                return MultiWayDeque.this.iterator(qId);
            }
        };
    }

    private void doubleCapacity() {
        int newSize = Math.max(next.length + 10, next.length * 2);
        next = Arrays.copyOf(next, newSize);
        prev = Arrays.copyOf(prev, newSize);
        values = Arrays.copyOf(values, newSize);
    }

    public void alloc() {
        alloc++;
        if (alloc >= next.length) {
            doubleCapacity();
        }
        next[alloc] = 0;
    }

    public int numberOfQueue() {
        return heads.length;
    }

    public void clear() {
        alloc = 0;
        Arrays.fill(heads, 0, queueNum, 0);
        Arrays.fill(tails, 0, queueNum, 0);
    }

    public boolean isEmpty(int qId) {
        return heads[qId] == 0;
    }

    public void expandQueueNum(int qNum) {
        if (qNum <= queueNum) {
        } else if (qNum <= heads.length) {
            Arrays.fill(heads, queueNum, qNum, 0);
            Arrays.fill(tails, queueNum, qNum, 0);
        } else {
            Arrays.fill(heads, queueNum, heads.length, 0);
            Arrays.fill(tails, queueNum, heads.length, 0);
            heads = Arrays.copyOf(heads, qNum);
            tails = Arrays.copyOf(tails, qNum);
        }
        queueNum = qNum;
    }

    public MultiWayDeque(int qNum, int totalCapacity) {
        values = new Object[totalCapacity + 1];
        next = new int[totalCapacity + 1];
        prev = new int[totalCapacity + 1];
        heads = new int[qNum];
        tails = new int[qNum];
        queueNum = qNum;
    }

    public void addLast(int qId, V x) {
        alloc();
        values[alloc] = x;

        if (heads[qId] == 0) {
            heads[qId] = tails[qId] = alloc;
            return;
        }
        next[tails[qId]] = alloc;
        prev[alloc] = tails[qId];
        tails[qId] = alloc;
    }

    public void addFirst(int qId, V x) {
        alloc();
        values[alloc] = x;

        if (heads[qId] == 0) {
            heads[qId] = tails[qId] = alloc;
            return;
        }
        next[alloc] = heads[qId];
        prev[heads[qId]] = alloc;
        heads[qId] = alloc;
    }

    public V peekFirst(int qId) {
        return (V) values[heads[qId]];
    }

    public V peekLast(int qId) {
        return (V) values[tails[qId]];
    }

    public V removeLast(int qId) {
        V ans = (V) values[tails[qId]];
        if (heads[qId] == tails[qId]) {
            heads[qId] = tails[qId] = 0;
        } else {
            tails[qId] = prev[tails[qId]];
            next[tails[qId]] = 0;
        }
        return ans;
    }

    public V removeFirst(int qId) {
        V ans = (V) values[heads[qId]];
        if (heads[qId] == tails[qId]) {
            heads[qId] = tails[qId] = 0;
        } else {
            heads[qId] = next[heads[qId]];
            prev[tails[qId]] = 0;
        }
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < queueNum; i++) {
            builder.append(i).append(": ");
            for (Iterator<V> iterator = iterator(i); iterator.hasNext(); ) {
                builder.append(iterator.next()).append(",");
            }
            if (builder.charAt(builder.length() - 1) == ',') {
                builder.setLength(builder.length() - 1);
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
