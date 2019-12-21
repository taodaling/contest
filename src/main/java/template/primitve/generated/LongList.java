package template.primitve.generated;

import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class LongList implements Cloneable{
    private int size;
    private int cap;
    private long[] data;
    private static final long[] EMPTY = new long[0];

    public long[] getData() {
        return data;
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

    public LongList(int cap) {
        this.cap = cap;
        if (cap == 0) {
            data = EMPTY;
        } else {
            data = new long[cap];
        }
    }

    public LongList(LongList list) {
        this.size = list.size;
        this.cap = list.cap;
        this.data = Arrays.copyOf(list.data, size);
    }

    public void insert(int i, int x) {
        if (i == size) {
            add(x);
            return;
        }
        checkRange(i);
        ensureSpace(size + 1);
        System.arraycopy(data, i, data, i + 1, size - i);
        data[i] = x;
        size++;
    }

    public LongList() {
        this(0);
    }

    public void reverse(int l, int r) {
        SequenceUtils.reverse(data, l, r);
    }

    public void reverse() {
        reverse(0, size - 1);
    }

    public void ensureSpace(int req) {
        if (req > cap) {
            while (cap < req) {
                cap = Math.max(cap + 10, 2 * cap);
            }
            data = Arrays.copyOf(data, cap);
        }
    }

    private void checkRange(int i) {
        if (i < 0 || i >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public long get(int i) {
        checkRange(i);
        return data[i];
    }

    public void add(long x) {
        ensureSpace(size + 1);
        data[size++] = x;
    }

    public void addAll(long[] x) {
        addAll(x, 0, x.length);
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

    public void expandWith(long x, int len) {
        ensureSpace(len);
        while (size < len) {
            data[size++] = x;
        }
    }

    public void sort() {
        if (size <= 1) {
            return;
        }
        Randomized.randomizedArray(data, 0, size);
        Arrays.sort(data, 0, size);
    }

    public long first() {
        checkRange(0);
        return data[0];
    }

    /**
     * Just retain the first n element in array, if there are not enough element, nothing will occur.
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

    public long tail() {
        checkRange(0);
        return data[size - 1];
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

    public void set(int i, int x) {
        checkRange(i);
        data[i] = x;
    }

    public int binarySearch(long x) {
        return Arrays.binarySearch(data, 0, size, x);
    }

    public int ceilBinarySearchIndex(long x) {
        return SequenceUtils.ceilIndex(data, x, 0, size - 1);
    }

    public int floorBinarySearchIndex(long x) {
        return SequenceUtils.floorIndex(data, x, 0, size - 1);
    }

    public int leftBound(long x) {
        return SequenceUtils.leftBound(data, x, 0, size - 1);
    }

    public int rightBound(long x) {
        return SequenceUtils.rightBound(data, x, 0, size - 1);
    }

    public void push(int x) {
        add(x);
    }

    public long pop() {
        return data[--size];
    }

    public int size() {
        return size;
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
        return Arrays.toString(toArray());
    }

    public LongIterator iterator() {
        return new LongIterator() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public long next() {
                return data[i++];
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LongList)) {
            return false;
        }
        LongList other = (LongList) obj;
        return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
    }

    @Override
    public int hashCode() {
        int h = 1;
        for (int i = 0; i < size; i++) {
            h = h * 31 + Long.hashCode(data[i]);
        }
        return h;
    }

    @Override
    public LongList clone() {
        LongList ans = new LongList();
        ans.addAll(this);
        return ans;
    }
}
