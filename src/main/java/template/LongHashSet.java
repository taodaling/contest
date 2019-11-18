package template;

import java.util.Arrays;

public class LongHashSet {
    private int[] slot;
    private int[] next;
    private long[] values;
    private int alloc;
    private boolean[] removed;
    private int mask;
    private int size;

    /**
     * You can only invoke mask times add operation
     */
    public LongHashSet(int cap) {
        this.mask = (1 << (32 - Integer.numberOfLeadingZeros(cap - 1))) - 1;
        slot = new int[mask + 1];
        next = new int[cap + 1];
        values = new long[cap + 1];
        removed = new boolean[cap + 1];
    }

    public void alloc() {
        alloc++;
        next[alloc] = 0;
        removed[alloc] = false;
        size++;
    }

    private int hash(long x) {
        int h = Long.hashCode(x);
        return h ^ (h >>> 16);
    }


    public void add(long x) {
        int h = hash(x);
        int s = h & mask;
        if (slot[s] == 0) {
            alloc();
            slot[s] = alloc;
            values[alloc] = x;
            return;
        }
        int index = findIndexOrLastEntry(s, x);
        if (values[index] != x) {
            alloc();
            next[index] = alloc;
            values[alloc] = x;
        }
    }

    public boolean contain(long x) {
        int h = hash(x);
        int s = h & mask;
        if (slot[s] == 0) {
            return false;
        }
        return values[findIndexOrLastEntry(s, x)] == x;
    }

    public void remove(long x) {
        int h = hash(x);
        int s = h & mask;
        if (slot[s] == 0) {
            return;
        }
        int index = findIndexOrLastEntry(s, x);
        if (values[index] != x) {
            return;
        }
        if (slot[s] == index) {
            slot[s] = next[index];
        }
        removed[index] = true;
        size--;
    }

    private int findIndexOrLastEntry(int s, long x) {
        int iter = slot[s];
        while (values[iter] != x) {
            if (next[iter] != 0) {
                iter = next[iter];
            } else {
                return iter;
            }
        }
        return iter;
    }

    public void clear() {
        alloc = 0;
        Arrays.fill(slot, 0);
        size = 0;
    }

    public LongIterator iterator() {
        return new LongIterator() {
            int index = 1;

            @Override
            public boolean hasNext() {
                while (index <= alloc && removed[index]) {
                    index++;
                }
                return index <= alloc;
            }

            @Override
            public long next() {
                if (!hasNext()) {
                    throw new IllegalStateException();
                }
                return values[index++];
            }
        };
    }

    public int size(){
        return size;
    }
}
