package template;

import java.util.Arrays;

public class IntHashMap {
    private int[] slot;
    private int[] next;
    private int[] keys;
    private int[] values;
    private int alloc;
    private boolean[] removed;
    private int mask;
    private int size;

    /**
     * You can only invoke mask times add operation
     */
    public IntHashMap(int cap) {
        this.mask = (1 << (32 - Integer.numberOfLeadingZeros(cap - 1))) - 1;
        slot = new int[mask + 1];
        next = new int[cap + 1];
        keys = new int[cap + 1];
        values = new int[cap + 1];
        removed = new boolean[cap + 1];
    }

    private void doubleCapacity() {
        int newSize = Math.max(next.length + 10, next.length * 2);
        next = Arrays.copyOf(next, newSize);
        keys = Arrays.copyOf(keys, newSize);
        values = Arrays.copyOf(values, newSize);
        removed = Arrays.copyOf(removed, newSize);
    }

    public void alloc() {
        alloc++;
        if (alloc >= next.length) {
            doubleCapacity();
        }
        next[alloc] = 0;
        removed[alloc] = false;
        size++;
    }

    private int hash(int x) {
        return x ^ (x >>> 16);
    }

    public void put(int x, int y) {
        int h = hash(x);
        int s = h & mask;
        if (slot[s] == 0) {
            alloc();
            slot[s] = alloc;
            keys[alloc] = x;
            values[alloc] = y;
            return;
        }
        int index = findIndexOrLastEntry(s, x);
        if (keys[index] != x) {
            alloc();
            next[index] = alloc;
            keys[alloc] = x;
            values[alloc] = y;
        }
    }

    public boolean containKey(int x) {
        int h = hash(x);
        int s = h & mask;
        if (slot[s] == 0) {
            return false;
        }
        return keys[findIndexOrLastEntry(s, x)] == x;
    }

    public int getOrDefault(int x, int def) {
        int h = hash(x);
        int s = h & mask;
        if (slot[s] == 0) {
            return def;
        }
        int index = findIndexOrLastEntry(s, x);
        return keys[index] == x ? values[index] : def;
    }

    public int get(int x) {
        return getOrDefault(x, 0);
    }

    public void remove(int x) {
        int h = hash(x);
        int s = h & mask;
        if (slot[s] == 0) {
            return;
        }
        int index = findIndexOrLastEntry(s, x);
        if (keys[index] != x) {
            return;
        }
        if (slot[s] == index) {
            slot[s] = next[index];
        }
        removed[index] = true;
        size--;
    }

    private int findIndexOrLastEntry(int s, int x) {
        int iter = slot[s];
        while (keys[iter] != x) {
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

    public IntEntryIterator iterator() {
        return new IntEntryIterator() {
            int index = 1;
            int readIndex = -1;

            @Override
            public boolean hasNext() {
                while (index <= alloc && removed[index]) {
                    index++;
                }
                return index <= alloc;
            }

            @Override
            public int getEntryKey() {
                return keys[readIndex];
            }

            @Override
            public int getEntryValue() {
                return values[readIndex];
            }

            @Override
            public void next() {
                if (!hasNext()) {
                    throw new IllegalStateException();
                }
                readIndex = index;
                index++;
            }
        };
    }

    public int size() {
        return size;
    }
}
