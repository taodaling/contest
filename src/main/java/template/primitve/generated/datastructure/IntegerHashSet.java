package template.primitve.generated.datastructure;


import template.rand.Hasher;

import java.util.Arrays;

public class IntegerHashSet {
    private int now;
    private int[] slot;
    private int[] version;
    private int[] next;
    private int[] keys;
    private int alloc;
    private boolean[] removed;
    private int mask;
    private int size;
    private boolean rehash;
    private Hasher hasher = new Hasher();

    public IntegerHashSet(int cap, boolean rehash) {
        now = 1;
        this.mask = (1 << (32 - Integer.numberOfLeadingZeros(cap - 1))) - 1;
        this.rehash = rehash;
        slot = new int[mask + 1];
        version = new int[slot.length + 1];
        next = new int[cap + 1];
        keys = new int[cap + 1];
        removed = new boolean[cap + 1];
    }

    private void access(int i) {
        if (version[i] != now) {
            version[i] = now;
            slot[i] = 0;
        }
    }

    private void doubleCapacity() {
        int newSize = Math.max(next.length + 10, next.length * 2);
        next = Arrays.copyOf(next, newSize);
        keys = Arrays.copyOf(keys, newSize);
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
        return hasher.hash(x);
    }

    public void add(int x) {
        int h = hash(x);
        int s = h & mask;
        access(s);
        if (slot[s] == 0) {
            alloc();
            slot[s] = alloc;
            keys[alloc] = x;
            return;
        } else {
            int index = findIndexOrLastEntry(s, x);
            if (keys[index] != x) {
                alloc();
                next[index] = alloc;
                keys[alloc] = x;
            }
        }
        if (rehash && size >= slot.length) {
            rehash();
        }
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
        this.version = newVersions;
        now = 0;
        this.mask = newMask;
    }


    public boolean contain(int x) {
        int h = hash(x);
        int s = h & mask;
        access(s);
        if (slot[s] == 0) {
            return false;
        }
        return keys[findIndexOrLastEntry(s, x)] == x;
    }

    public void remove(int x) {
        int h = hash(x);
        int s = h & mask;
        access(s);
        if (slot[s] == 0) {
            return;
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
            return;
        }
        if (slot[s] == index) {
            slot[s] = next[index];
        } else {
            next[pre] = next[index];
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
        size = 0;
        now++;
    }

    public IntegerIterator iterator() {
        return new IntegerIterator() {
            int index = 1;

            @Override
            public boolean hasNext() {
                while (index <= alloc && removed[index]) {
                    index++;
                }
                return index <= alloc;
            }

            @Override
            public int next() {
                if (!hasNext()) {
                    throw new IllegalStateException();
                }
                return keys[index++];
            }
        };
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        IntegerIterator iterator = iterator();
        StringBuilder builder = new StringBuilder("{");
        while (iterator.hasNext()) {
            builder.append(iterator.next()).append(',');
        }
        if (builder.charAt(builder.length() - 1) == ',') {
            builder.setLength(builder.length() - 1);
        }
        builder.append('}');
        return builder.toString();
    }
}
