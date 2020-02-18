package template.primitve.datastructure;

import template.primitve.generated.LongDeque;
import template.primitve.generated.LongIterator;

public class LongDequeImpl implements LongDeque {
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

    public template.primitve.generated.LongIterator iterator() {
        return new template.primitve.generated.LongIterator() {
            int index = bpos;

            @Override
            public boolean hasNext() {
                return index != epos;
            }

            @Override
            public long next() {
                long ans = data[index];
                index = LongDequeImpl.this.next(index);
                return ans;
            }
        };
    }

    public long removeFirst(){
        long ans = data[bpos];
        bpos = next(bpos);
        return ans;
    }

    public long removeLast(){
        long ans = data[last(epos)];
        epos = last(epos);
        return ans;
    }

    public void addLast(long x) {
        ensureMore();
        data[epos] = x;
        epos = next(epos);
    }

    public void addFirst(long x) {
        ensureMore();
        bpos = last(bpos);
        data[bpos] = x;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (LongIterator iterator = iterator(); iterator.hasNext(); ) {
            builder.append(iterator.next()).append(' ');
        }
        return builder.toString();
    }
}
