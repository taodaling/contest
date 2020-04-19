import java.util.Arrays;

public class SteelMill {
    public long cheapest(int goal, int[] shipmentCost, int[] shipmentSize, int[] costPerKg) {
        long[] last = new long[goal + 1];
        long[] next = new long[goal + 1];
        long inf = (long) 1e18;
        Arrays.fill(last, inf);
        last[0] = 0;

        int n = shipmentCost.length;
        LongMinQueue dq = new LongMinQueue(goal + 1, LongComparator.NATURE_ORDER);
        for (int i = 0; i < n; i++) {
            dq.reset();
            dq.addLast(0);
            for (int j = 1; j <= goal; j++) {
                long extra = (long) j * costPerKg[i];
                next[j] = Math.min(last[j], dq.min() + extra + shipmentCost[i]);
                dq.addLast(last[j] - extra);
                if (dq.size() > shipmentSize[i]) {
                    dq.removeFirst();
                }
            }

            long[] tmp = last;
            last = next;
            next = tmp;
        }

        return last[goal];
    }

}

class LongDequeImpl implements LongDeque {
    private long[] data;
    private int bpos;
    private int epos;
    private static final long[] EMPTY = new long[0];
    private int n;

    public LongDequeImpl(int cap) {
        if (cap == 0) {
            data = EMPTY;
        } else {
            data = new long[cap];
        }
        bpos = 0;
        epos = 0;
        n = cap;
    }

    private void expandSpace(int len) {
        while (n < len) {
            n = Math.max(n + 10, n * 2);
        }
        long[] newData = new long[n];
        if (bpos <= epos) {
            if (bpos < epos) {
                System.arraycopy(data, bpos, newData, 0, epos - bpos);
            }
        } else {
            System.arraycopy(data, bpos, newData, 0, data.length - bpos);
            System.arraycopy(data, 0, newData, data.length - bpos, epos);
        }
        epos = size();
        bpos = 0;
        data = newData;
    }

    public LongIterator iterator() {
        return new LongIterator() {
            int index = bpos;


            public boolean hasNext() {
                return index != epos;
            }


            public long next() {
                long ans = data[index];
                index = LongDequeImpl.this.next(index);
                return ans;
            }
        };
    }

    public long removeFirst() {
        long ans = data[bpos];
        bpos = next(bpos);
        return ans;
    }

    public long removeLast() {
        long ans = data[last(epos)];
        epos = last(epos);
        return ans;
    }

    public void addLast(long x) {
        ensureMore();
        data[epos] = x;
        epos = next(epos);
    }

    public long peekFirst() {
        return data[bpos];
    }

    public long peekLast() {
        return data[last(epos)];
    }

    public void clear() {
        bpos = epos = 0;
    }

    private int last(int x) {
        return (x == 0 ? n : x) - 1;
    }

    private int next(int x) {
        return x + 1 >= n ? 0 : x + 1;
    }

    private void ensureMore() {
        if (next(epos) == bpos) {
            expandSpace(n + 1);
        }
    }

    public int size() {
        int ans = epos - bpos;
        if (ans < 0) {
            ans += data.length;
        }
        return ans;
    }

    public boolean isEmpty() {
        return bpos == epos;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (LongIterator iterator = iterator(); iterator.hasNext(); ) {
            builder.append(iterator.next()).append(' ');
        }
        return builder.toString();
    }

}

interface LongComparator {
    public static final LongComparator NATURE_ORDER = (a, b) -> Long.compare(a, b);

    public int compare(long a, long b);

}

interface LongStack {
}

class LongMinQueue {
    LongDequeImpl minQueue;
    LongDequeImpl data;
    LongComparator comparator;

    public LongMinQueue(int cap, LongComparator comparator) {
        minQueue = new LongDequeImpl(cap);
        data = new LongDequeImpl(cap);
        this.comparator = comparator;
    }

    public void reset() {
        minQueue.clear();
        data.clear();
    }

    public void addLast(long val) {
        data.addLast(val);
        while (!minQueue.isEmpty() && comparator.compare(minQueue.peekLast(), val) > 0) {
            minQueue.removeLast();
        }
        minQueue.addLast(val);
    }

    public long removeFirst() {
        long val = data.removeFirst();
        if (minQueue.peekFirst() == val) {
            minQueue.removeFirst();
        }
        return val;
    }

    public int size() {
        return data.size();
    }

    public long min() {
        return minQueue.peekFirst();
    }

}

interface LongIterator {
    boolean hasNext();

    long next();

}

interface LongDeque extends LongStack {
}
