package template.datastructure;

import java.util.Arrays;

public class MultiWayIntStack {
    private int[] values;
    private int[] next;
    private int[] heads;
    private int alloc;
    private int queueNum;

    public IntIterator iterator(final int queue) {
        return new IntIterator() {
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

    public int queueNumber() {
        return queueNum;
    }

    public void clear() {
        alloc = 0;
        Arrays.fill(heads, 0, queueNum, 0);
    }

    public boolean isEmpty(int qId) {
        return heads[qId] == 0;
    }

    public void expandQueueNum(int qNum) {
        if (qNum <= queueNum) {
        } else if (qNum <= heads.length) {
            Arrays.fill(heads, queueNum, qNum, 0);
        } else {
            Arrays.fill(heads, queueNum, heads.length, 0);
            heads = Arrays.copyOf(heads, qNum);
        }
        queueNum = qNum;
    }

    public MultiWayIntStack(int qNum, int totalCapacity) {
        values = new int[totalCapacity + 1];
        next = new int[totalCapacity + 1];
        heads = new int[qNum];
        queueNum = qNum;
    }

    public void addFirst(int qId, int x) {
        alloc();
        values[alloc] = x;
        next[alloc] = heads[qId];
        heads[qId] = alloc;
    }


    public int peekFirst(int qId) {
        return values[heads[qId]];
    }


    public int removeFirst(int qId) {
        int ans = values[heads[qId]];
        heads[qId] = next[heads[qId]];
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < queueNum; i++){
            builder.append(i).append(": ");
            for(IntIterator iterator = iterator(i); iterator.hasNext(); ){
                builder.append(iterator.next()).append(",");
            }
            if(builder.charAt(builder.length() - 1) == ','){
                builder.setLength(builder.length() - 1);
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
