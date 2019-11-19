package template;

import java.util.Arrays;

public class LongList {
    private int size;
    private int cap;
    private long[] data;
    private static final long[] EMPTY = new long[0];

    public LongList(int cap) {
        this.cap = cap;
        if (cap == 0) {
            data = EMPTY;
        } else {
            data = new long[cap];
        }
    }

    public int ceilBinarySearchIndex(long x) {
        return SequenceUtils.ceilIndex(data, x, 0, size - 1);
    }

    public int floorBinarySearchIndex(long x) {
        return SequenceUtils.floorIndex(data, x, 0, size - 1);
    }

    public long[] getData() {
        return data;
    }

    /**
     * Just retain the first n element in array, if there are not enough element,
     * nothing will occur.
     */
    public void retain(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n >= size) {
            return;
        }
        size = n;
    }

    public LongList(LongList list) {
        this.size = list.size;
        this.cap = list.cap;
        this.data = Arrays.copyOf(list.data, size);
    }

    public LongList() {
        this(0);
    }

    public void ensureSpace(int req) {
        if (req > cap) {
            while (cap < req) {
                cap = Math.max(cap + 10, 2 * cap);
            }
            data = Arrays.copyOf(data, cap);
        }
    }

    public void set(int i, long x) {
        checkRange(i);
        data[i] = x;
    }

    private void checkRange(int i) {
        if (i < 0 || i >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public void expandWith(long x, int len) {
        ensureSpace(len);
        while (size < len) {
            add(x);
        }
    }

    public long get(int i) {
        checkRange(i);
        return data[i];
    }

    public long first() {
        checkRange(0);
        return data[0];
    }

    public long tail() {
        checkRange(0);
        return data[size - 1];
    }

    public void add(long x) {
        ensureSpace(size + 1);
        data[size++] = x;
    }

    public void addAll(long[] x, int offset, int len) {
        ensureSpace(size + len);
        System.arraycopy(x, offset, data, size, len);
        size += len;
    }

    public void addAll(LongList list) {
        addAll(list.data, 0, list.size);
    }

    public int indexOf(long x) {
        for (int i = 0; i < size; i++) {
            if (x == data[i]) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(long x) {
        for (int i = size - 1; i >= 0; i--) {
            if (x == data[i]) {
                return i;
            }
        }
        return -1;
    }

    public void sort() {
        if (size <= 1) {
            return;
        }
        if (size < 256) {
            Randomized.randomizedArray(data, 0, size);
            Arrays.sort(data, 0, size);
        } else {
            CompareUtils.radixSort(data, 0, size - 1);
        }
    }

    public void unique() {
        if (size <= 1) {
            return;
        }

        sort();
        int wpos = 1;
        for (int i = 1; i < size; i++) {
            if (data[i] != data[wpos - 1]) {
                data[wpos++] = data[i];
            }
        }
        size = wpos;
    }

    public int binarySearch(long x) {
        return Arrays.binarySearch(data, 0, size, x);
    }

    public void push(long x) {
        add(x);
    }

    public long pop() {
        return data[--size];
    }

    public int size() {
        return size;
    }

    public void remove(int l, int r) {
        checkRange(l);
        checkRange(r);
        if (l > r) {
            return;
        }
        if (r == size - 1) {
            size = l;
            return;
        } else {
            System.arraycopy(data, l, data, r + 1, size - (r + 1));
            size -= (r - l + 1);
        }
    }

    public void remove(int index) {
        checkRange(index);
        if (index == size - 1) {
            pop();
            return;
        }
        System.arraycopy(data, index + 1, data, index, size - index);
        size--;
    }

    public long[] toArray() {
        return Arrays.copyOf(data, size);
    }

    public long[] toArray(long[] arr) {
        if (arr.length < size) {
            arr = new long[size];
        }
        System.arraycopy(data, 0, arr, 0, size);
        return arr;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
    }

    public String toString() {
        return Arrays.toString(Arrays.copyOf(data, size));
    }
}
