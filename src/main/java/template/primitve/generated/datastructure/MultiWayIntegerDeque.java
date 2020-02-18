package template.primitve.generated.datastructure;

import java.util.Arrays;

public class MultiWayIntegerDeque {
    private int[] values;
    private int[] next;
    private int[] prev;
    private int[] heads;
    private int[] tails;
    private int alloc;
    private int queueNum;

    public IntegerIterator iterator(final int queue) {
        return new IntegerIterator() {
            int ele = heads[queue];

            @Override
            public boolean hasNext() {
                return ele != 0;
            }

            @Override
            public int next() {
                int ans = values[ele];
                ele = next[ele];
                return ans;
            }
        };
    }

    public IntegerDeque getDeque(int qId) {
        return new IntegerDeque() {
            @Override
            public void addFirst(int x) {
                MultiWayIntegerDeque.this.addFirst(qId, x);
            }

            @Override
            public int removeFirst() {
                return MultiWayIntegerDeque.this.removeFirst(qId);
            }

            @Override
            public int peekFirst() {
                return MultiWayIntegerDeque.this.peekFirst(qId);
            }

            @Override
            public void addLast(int x) {
                MultiWayIntegerDeque.this.addLast(qId, x);
            }

            @Override
            public int removeLast() {
                return MultiWayIntegerDeque.this.removeLast(qId);
            }

            @Override
            public int peekLast() {
                return MultiWayIntegerDeque.this.peekLast(qId);
            }

            @Override
            public IntegerIterator iterator() {
                return MultiWayIntegerDeque.this.iterator(qId);
            }

            @Override
            public boolean isEmpty() {
                return MultiWayIntegerDeque.this.isEmpty(qId);
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

    public MultiWayIntegerDeque(int qNum, int totalCapacity) {
        values = new int[totalCapacity + 1];
        next = new int[totalCapacity + 1];
        prev = new int[totalCapacity + 1];
        heads = new int[qNum];
        tails = new int[qNum];
        queueNum = qNum;
    }

    public void addLast(int qId, int x) {
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

    public void addFirst(int qId, int x) {
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

    public int peekFirst(int qId) {
        return values[heads[qId]];
    }

    public int peekLast(int qId) {
        return values[tails[qId]];
    }

    public int removeLast(int qId) {
        int ans = values[tails[qId]];
        if (heads[qId] == tails[qId]) {
            heads[qId] = tails[qId] = 0;
        } else {
            tails[qId] = prev[tails[qId]];
            next[tails[qId]] = 0;
        }
        return ans;
    }

    public int removeFirst(int qId) {
        int ans = values[heads[qId]];
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
            for (IntegerIterator iterator = iterator(i); iterator.hasNext(); ) {
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
