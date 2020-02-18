package template.primitve.datastructure;

import template.primitve.generated.LongDeque;
import template.primitve.generated.LongIterator;

import java.util.Arrays;

public class LongMultiWayDeque {
    private long[] values;
    private int[] next;
    private int[] prev;
    private int[] heads;
    private int[] tails;
    private int alloc;
    private int queueNum;

    public template.primitve.generated.LongIterator iterator(final int queue) {
        return new template.primitve.generated.LongIterator() {
            int ele = heads[queue];

            @Override
            public boolean hasNext() {
                return ele != 0;
            }

            @Override
            public long next() {
                long ans = values[ele];
                ele = next[ele];
                return ans;
            }
        };
    }

    public template.primitve.generated.LongDeque getDeque(int qId) {
        return new LongDeque() {
            @Override
            public void addFirst(long x) {
                LongMultiWayDeque.this.addFirst(qId, x);
            }

            @Override
            public long removeFirst() {
                return LongMultiWayDeque.this.removeFirst(qId);
            }

            @Override
            public long peekFirst() {
                return LongMultiWayDeque.this.peekFirst(qId);
            }

            @Override
            public void addLast(long x) {
                LongMultiWayDeque.this.addLast(qId, x);
            }

            @Override
            public long removeLast() {
                return LongMultiWayDeque.this.removeLast(qId);
            }

            @Override
            public long peekLast() {
                return LongMultiWayDeque.this.peekLast(qId);
            }

            @Override
            public template.primitve.generated.LongIterator iterator() {
                return LongMultiWayDeque.this.iterator(qId);
            }

            @Override
            public boolean isEmpty() {
                return LongMultiWayDeque.this.isEmpty(qId);
            }

            @Override
            public void clear() {
                heads[qId] = 0;
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
        return queueNum;
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

    public LongMultiWayDeque(int qNum, int totalCapacity) {
        values = new long[totalCapacity + 1];
        next = new int[totalCapacity + 1];
        prev = new int[totalCapacity + 1];
        heads = new int[qNum];
        tails = new int[qNum];
        queueNum = qNum;
    }

    public void addLast(int qId, long x) {
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

    public void addFirst(int qId, long x) {
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

    public long peekFirst(int qId) {
        return values[heads[qId]];
    }

    public long peekLast(int qId) {
        return values[tails[qId]];
    }

    public long removeLast(int qId) {
        long ans = values[tails[qId]];
        if (heads[qId] == tails[qId]) {
            heads[qId] = tails[qId] = 0;
        } else {
            tails[qId] = prev[tails[qId]];
            next[tails[qId]] = 0;
        }
        return ans;
    }

    public long removeFirst(int qId) {
        long ans = values[heads[qId]];
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
            for (LongIterator iterator = iterator(i); iterator.hasNext(); ) {
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
