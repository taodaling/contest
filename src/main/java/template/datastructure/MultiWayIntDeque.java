package template.datastructure;

import java.util.Arrays;

public class MultiWayIntDeque {
    private int[] vals;
    private int[] next;
    private int[] prev;
    private int[] heads;
    private int[] tails;
    private int alloc;
    private int queueNum;

    private void doubleCapacity() {
        int newSize = Math.max(next.length + 10, next.length * 2);
        next = Arrays.copyOf(next, newSize);
        prev = Arrays.copyOf(prev, newSize);
        vals = Arrays.copyOf(vals, newSize);
    }

    public void alloc() {
        alloc++;
        if (alloc >= next.length) {
            doubleCapacity();
        }
        next[alloc] = 0;
    }

    public int queueNumber() {
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

    public MultiWayIntDeque(int qNum, int totalCapacity) {
        vals = new int[totalCapacity + 1];
        next = new int[totalCapacity + 1];
        prev = new int[totalCapacity + 1];
        heads = new int[qNum];
        tails = new int[qNum];
        queueNum = qNum;
    }

    public void addLast(int qId, int x) {
        alloc();
        vals[alloc] = x;

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
        vals[alloc] = x;

        if (heads[qId] == 0) {
            heads[qId] = tails[qId] = alloc;
            return;
        }
        next[alloc] = heads[qId];
        prev[heads[qId]] = alloc;
        heads[qId] = alloc;
    }

    public int peekFirst(int qId) {
        return vals[heads[qId]];
    }

    public int peekLast(int qId) {
        return vals[tails[qId]];
    }

    public int removeLast(int qId) {
        int ans = vals[tails[qId]];
        if (heads[qId] == tails[qId]) {
            heads[qId] = tails[qId] = 0;
        } else {
            tails[qId] = prev[tails[qId]];
            next[tails[qId]] = 0;
        }
        return ans;
    }

    public int removeFirst(int qId) {
        int ans = vals[heads[qId]];
        if (heads[qId] == tails[qId]) {
            heads[qId] = tails[qId] = 0;
        } else {
            heads[qId] = next[heads[qId]];
            prev[tails[qId]] = 0;
        }
        return ans;
    }
}
