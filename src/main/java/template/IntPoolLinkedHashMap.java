package template;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

public class IntPoolLinkedHashMap {
    public IntPoolLinkedHashMap(Supplier<Entry[]> slotSupplier, Supplier<Entry> entrySupplier, Hash hash, EqualChecker equalChecker, int bit) {
        this.slotSupplier = slotSupplier;
        this.entrySupplier = entrySupplier;
        this.hash = hash;
        this.equalChecker = equalChecker;

        if (this.slotSupplier == null) {
            this.slotSupplier = () -> new Entry[128];
        }
        if (this.entrySupplier == null) {
            this.entrySupplier = Entry::new;
        }
        if (this.hash == null) {
            this.hash = x -> x;
        }
        if (this.equalChecker == null) {
            this.equalChecker = (a, b) -> a == b;
        }

        this.slots = this.slotSupplier.get();
        if ((1 << bit) > this.slots.length) {
            throw new IllegalArgumentException();
        }
        mask = (1 << bit) - 1;

        linkedListHead = this.entrySupplier.get();
        linkedListHead.linkedListFormer = linkedListHead;
        linkedListHead.linkedListNext = linkedListHead;
    }

    public static class Entry {
        private Entry linkedListNext;
        private Entry linkedListFormer;
        private Entry slotNext;
        private int h;
        private int key;
        private int value;

        public void clear() {
            linkedListNext = null;
            linkedListFormer = null;
            slotNext = null;
            key = 0;
            value = 0;
            h = 0;
        }

        public int getKey() {
            return key;
        }

        public int getValue() {
            return value;
        }

        public int setValue(int value) {
            int old = this.value;
            this.value = value;
            return old;
        }
    }

    public static interface Hash<K> {
        int hash(int k);
    }

    public static interface EqualChecker<K> {
        boolean equal(int a, int b);
    }

    public static interface RewindableSupplier<T> extends Supplier<T> {
        void rewind();
    }

    public int get(int key) {
        return getOrDefault(key, 0);
    }

    private Entry searchSlot(int index, int h, int key) {
        Entry last = slots[index];
        while (last != null && (last.h != h || !equalChecker.equal(last.key, key))) {
            last = last.slotNext;
        }
        return last;
    }

    public int put(int key, int value) {
        int h = hash(key);
        int slotNum = h & mask;

        Entry entry = searchSlot(slotNum, h, key);
        if (entry != null) {
            int old = entry.value;
            entry.value = value;
            return old;
        }

        entry = new Entry();
        entry.value = value;
        entry.key = key;
        entry.h = h;

        entry.slotNext = slots[slotNum];
        slots[slotNum] = entry;

        linkedListHead.linkedListFormer.linkedListNext = entry;
        entry.linkedListFormer = linkedListHead.linkedListFormer;
        linkedListHead.linkedListFormer = entry;
        entry.linkedListNext = linkedListHead;

        return 0;
    }

    public int remove(int key) {
        int h = hash(key);
        int slotNum = h & mask;

        Entry last = null;
        Entry entry = slots[slotNum];
        while (entry != null && (entry.h != h || !equalChecker.equal(entry.key, key))) {
            last = entry;
            entry = entry.slotNext;
        }

        if (entry == null) {
            return 0;
        }

        //Remove from slot
        if (last == null) {
            slots[slotNum] = entry.slotNext;
        } else {
            last.slotNext = entry.slotNext;
        }

        //Remove from linkedlist
        entry.linkedListNext.linkedListFormer = entry.linkedListFormer;
        entry.linkedListFormer.linkedListNext = entry.linkedListNext;

        return entry.value;
    }

    private final int hash(int key) {
        int h;
        return (h = hash.hash(key)) ^ (h >>> 16);
    }

    public boolean containsKey(int key) {
        int h = hash(key);
        int slotNum = h & mask;
        return searchSlot(slotNum, h, key) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        Arrays.fill(slots, 0, mask + 1, null);
        size = 0;
        linkedListHead.linkedListNext = linkedListHead.linkedListFormer = linkedListHead;
    }

    public Set<Entry> entrySet() {
        if (set == null) {
            set = new AbstractSet<Entry>() {
                @Override
                public Iterator<Entry> iterator() {
                    return new Iterator<Entry>() {
                        Entry head = linkedListHead;

                        @Override
                        public boolean hasNext() {
                            return head.linkedListNext != linkedListHead;
                        }

                        @Override
                        public Entry next() {
                            return head = head.linkedListNext;
                        }
                    };
                }

                @Override
                public int size() {
                    return size;
                }
            };
        }
        return set;
    }

    public int getOrDefault(int key, int defaultValue) {
        int h = hash(key);
        int slotNum = h & mask;

        Entry entry = searchSlot(slotNum, h, key);
        if (entry == null) {
            return defaultValue;
        }
        return entry.value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        for (Entry entry : entrySet()) {
            builder.append(entry.key).append("=>").append(entry.value).append(",");
        }
        if (builder.charAt(builder.length() - 1) == ',') {
            builder.setLength(builder.length() - 1);
        }
        builder.append("}");
        return builder.toString();
    }

    private Entry[] slots;
    private Supplier<Entry[]> slotSupplier;
    private Supplier<Entry> entrySupplier;
    private Hash hash;
    private EqualChecker equalChecker;
    private int mask;
    private int size;
    private Entry linkedListHead;
    private Set<Entry> set;
}
