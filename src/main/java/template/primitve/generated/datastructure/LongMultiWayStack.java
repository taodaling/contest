package template.primitve.generated.datastructure;


import java.util.Arrays;

public class LongMultiWayStack {
    private long[] values;
    private int[] next;
    private int[] heads;
    private int alloc;
    private int stackNum;

    public LongIterator iterator(final int queue) {
        return new LongIterator() {
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

    public LongStack getStack(int qId) {
        return new LongStack() {
            @Override
            public void addLast(long x) {
                LongMultiWayStack.this.addLast(qId, x);
            }

            @Override
            public long removeLast() {
                return LongMultiWayStack.this.removeLast(qId);
            }

            @Override
            public long peekLast() {
                return LongMultiWayStack.this.peekLast(qId);
            }

            @Override
            public LongIterator iterator() {
                return LongMultiWayStack.this.iterator(qId);
            }

            @Override
            public boolean isEmpty() {
                return LongMultiWayStack.this.isEmpty(qId);
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
        values = Arrays.copyOf(values, newSize);
    }

    public void alloc() {
        alloc++;
        if (alloc >= next.length) {
            doubleCapacity();
        }
        next[alloc] = 0;
    }

    public int stackNumber() {
        return stackNum;
    }

    public void clear() {
        alloc = 0;
        Arrays.fill(heads, 0, stackNum, 0);
    }

    public boolean isEmpty(int qId) {
        return heads[qId] == 0;
    }

    public void expandStackNum(int qNum) {
        if (qNum <= stackNum) {
        } else if (qNum <= heads.length) {
            Arrays.fill(heads, stackNum, qNum, 0);
        } else {
            Arrays.fill(heads, stackNum, heads.length, 0);
            heads = Arrays.copyOf(heads, qNum);
        }
        stackNum = qNum;
    }

    public LongMultiWayStack(int qNum, int totalCapacity) {
        values = new long[totalCapacity + 1];
        next = new int[totalCapacity + 1];
        heads = new int[qNum];
        stackNum = qNum;
    }

    public void addLast(int qId, long x) {
        alloc();
        values[alloc] = x;
        next[alloc] = heads[qId];
        heads[qId] = alloc;
    }


    public long peekLast(int qId) {
        return values[heads[qId]];
    }


    public long removeLast(int qId) {
        long ans = values[heads[qId]];
        heads[qId] = next[heads[qId]];
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < stackNum; i++) {
            if (isEmpty(i)) {
                continue;
            }
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
