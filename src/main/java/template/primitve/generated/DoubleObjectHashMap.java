package template.primitve.generated;

import java.util.Arrays;

public class DoubleObjectHashMap<V> {
    private int[] slot;
    private int[] next;
    private double[] keys;
    private Object[] values;
    private int alloc;
    private boolean[] removed;
    private int mask;
    private int size;
    private boolean rehash;

    public DoubleObjectHashMap(int cap, boolean rehash) {
        this.mask = (1 << (32 - Integer.numberOfLeadingZeros(cap - 1))) - 1;
        slot = new int[mask + 1];
        next = new int[cap + 1];
        keys = new double[cap + 1];
        values = new Object[cap + 1];
        removed = new boolean[cap + 1];
        this.rehash = rehash;
    }

    private void doubleCapacity() {
        int newSize = Math.max(next.length + 10, next.length * 2);
        next = Arrays.copyOf(next, newSize);
        keys = Arrays.copyOf(keys, newSize);
        values = Arrays.copyOf(values, newSize);
        removed = Arrays.copyOf(removed, newSize);
    }

    private void rehash() {
        int[] newSlots = new int[Math.max(16, slot.length * 2)];
        int newMask = newSlots.length - 1;
        for (int i = 0; i < slot.length; i++) {
            if (slot[i] == 0) {
                continue;
            }
            int head = slot[i];
            while (head != 0) {
                int n = next[head];
                int s = hash(keys[head]) & newMask;
                next[head] = newSlots[s];
                newSlots[s] = head;
                head = n;
            }
        }
        this.slot = newSlots;
        this.mask = newMask;
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

    private int hash(double x) {
        int h = Double.hashCode(x);
        return h ^ (h >>> 16);
    }

    public void put(double x, V y) {
        int h = hash(x);
        int s = h & mask;
        if (slot[s] == 0) {
            alloc();
            slot[s] = alloc;
            keys[alloc] = x;
            values[alloc] = y;
        } else {
            int index = findIndexOrLastEntry(s, x);
            if (keys[index] != x) {
                alloc();
                next[index] = alloc;
                keys[alloc] = x;
                values[alloc] = y;
            } else {
                values[index] = y;
            }
        }
        if (rehash && size >= slot.length) {
            rehash();
        }
    }


    public boolean containKey(double x) {
        int h = hash(x);
        int s = h & mask;
        if (slot[s] == 0) {
            return false;
        }
        return keys[findIndexOrLastEntry(s, x)] == x;
    }

    public V getOrDefault(double x, V def) {
        int h = hash(x);
        int s = h & mask;
        if (slot[s] == 0) {
            return def;
        }
        int index = findIndexOrLastEntry(s, x);
        return keys[index] == x ? (V) values[index] : def;
    }

    public V get(double x) {
        return getOrDefault(x, null);
    }

    public V remove(double x) {
        int h = hash(x);
        int s = h & mask;
        if (slot[s] == 0) {
            return null;
        }
        int pre = 0;
        int index = slot[s];
        while (keys[index] != x) {
            pre = index;
            if (next[index] != 0) {
                index = next[index];
            } else {
                break;
            }
        }
        if (keys[index] != x) {
            return null;
        }
        if (slot[s] == index) {
            slot[s] = next[index];
        } else {
            next[pre] = next[index];
        }
        removed[index] = true;
        size--;
        return (V) values[index];
    }

    private int findIndexOrLastEntry(int s, double x) {
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

    public DoubleObjectEntryIterator<V> iterator() {
        return new DoubleObjectEntryIterator() {
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
            public double getEntryKey() {
                return keys[readIndex];
            }

            @Override
            public Object getEntryValue() {
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

    @Override
    public String toString() {
        DoubleObjectEntryIterator<V> iterator = iterator();
        StringBuilder builder = new StringBuilder("{");
        while (iterator.hasNext()) {
            iterator.next();
            builder.append(iterator.getEntryKey()).append("->").append(iterator.getEntryValue()).append(',');
        }
        if (builder.charAt(builder.length() - 1) == ',') {
            builder.setLength(builder.length() - 1);
        }
        builder.append('}');
        return builder.toString();
    }
}
