package template.primitve.generated.datastructure;


import template.rand.Hasher;

import java.util.Arrays;

public class IntegerObjectHashMap<V> {
    private int[] slot;
    private int[] next;
    private int[] keys;
    private Object[] values;
    private int alloc;
    private boolean[] removed;
    private int mask;
    private int size;
    private boolean rehash;
    private Hasher hasher = new Hasher();
    private int[] version;
    private int now;

    private void access(int i) {
        if (version[i] != now) {
            slot[i] = 0;
            version[i] = now;
        }
    }

    public IntegerObjectHashMap(int cap, boolean rehash) {
        this.mask = (1 << (32 - Integer.numberOfLeadingZeros(cap - 1))) - 1;
        slot = new int[mask + 1];
        version = new int[mask + 1];
        next = new int[cap + 1];
        keys = new int[cap + 1];
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
        int[] newVersions = new int[newSlots.length];
        int newMask = newSlots.length - 1;
        for (int i = 0; i < slot.length; i++) {
            access(i);
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
        this.version = newVersions;
        now = 0;
    }

    private void alloc() {
        alloc++;
        if (alloc >= next.length) {
            doubleCapacity();
        }
        next[alloc] = 0;
        removed[alloc] = false;
        values[alloc] = null;
        size++;
    }

    private int hash(int x) {
        return hasher.hash(x);
    }

    public void put(int x, V y) {
        int h = hash(x);
        int s = h & mask;
        access(s);
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


    public boolean containKey(int x) {
        int h = hash(x);
        int s = h & mask;
        access(s);
        if (slot[s] == 0) {
            return false;
        }
        return keys[findIndexOrLastEntry(s, x)] == x;
    }

    public V getOrDefault(int x, V def) {
        int h = hash(x);
        int s = h & mask;
        access(s);
        if (slot[s] == 0) {
            return def;
        }
        int index = findIndexOrLastEntry(s, x);
        return keys[index] == x ? (V) values[index] : def;
    }

    public V get(int x) {
        return getOrDefault(x, null);
    }

    public V remove(int x) {
        int h = hash(x);
        int s = h & mask;
        access(s);
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
        size = 0;
        now++;
    }

    public IntegerObjectEntryIterator<V> iterator() {
        return new IntegerObjectEntryIterator() {
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
        IntegerObjectEntryIterator<V> iterator = iterator();
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
