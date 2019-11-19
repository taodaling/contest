package template;

import java.util.Arrays;

public class IntList {
    private int size;
    private int cap;
    private int[] data;
    private static final int[] EMPTY = new int[0];

    public int[] getData() {
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

    public IntList(int cap) {
        this.cap = cap;
        if (cap == 0) {
            data = EMPTY;
        } else {
            data = new int[cap];
        }
    }

    public IntList(IntList list) {
        this.size = list.size;
        this.cap = list.cap;
        this.data = Arrays.copyOf(list.data, size);
    }

    public IntList() {
        this(0);
    }

    public void reverse(int l, int r) {
        SequenceUtils.reverse(data, l, r);
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

    public int get(int i) {
        checkRange(i);
        return data[i];
    }

    public void add(int x) {
        ensureSpace(size + 1);
        data[size++] = x;
    }

    public void addAll(int[] x) {
        addAll(x, 0, x.length);
    }

    public void addAll(int[] x, int offset, int len) {
        ensureSpace(size + len);
        System.arraycopy(x, offset, data, size, len);
        size += len;
    }

    public void addAll(IntList list) {
        addAll(list.data, 0, list.size);
    }

    public int indexOf(int x) {
        for (int i = 0; i < size; i++) {
            if (x == data[i]) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(int x) {
        for (int i = size - 1; i >= 0; i--) {
            if (x == data[i]) {
                return i;
            }
        }
        return -1;
    }

    public void expandWith(int x, int len) {
        ensureSpace(len);
        while (size < len) {
            data[size++] = x;
        }
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

    public int first() {
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

    public int tail() {
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

    public int binarySearch(int x) {
        return Arrays.binarySearch(data, 0, size, x);
    }

    public int ceilBinarySearchIndex(int x) {
        return SequenceUtils.ceilIndex(data, x, 0, size - 1);
    }

    public int floorBinarySearchIndex(int x) {
        return SequenceUtils.floorIndex(data, x, 0, size - 1);
    }

    public void push(int x) {
        add(x);
    }

    public int pop() {
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

    public int[] toArray() {
        return Arrays.copyOf(data, size);
    }

    public int[] toArray(int[] arr) {
        if (arr.length < size) {
            arr = new int[size];
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntList)) {
            return false;
        }
        IntList other = (IntList) obj;
        return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
    }
}
