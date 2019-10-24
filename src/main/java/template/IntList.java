package template;

import java.util.Arrays;

public class IntList {
    private int size;
    private int cap;
    private int[] data;
    private static final int[] EMPTY = new int[0];

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

    private void ensureSpace(int need) {
        int req = size + need;
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
        ensureSpace(1);
        data[size++] = x;
    }

    public void addAll(int[] x, int offset, int len) {
        ensureSpace(len);
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
        ensureSpace(len - size);
        while (size < len) {
            add(x);
        }
    }

    public void sort() {
        if (size <= 1) {
            return;
        }
        Randomized.randomizedArray(data, 0, size);
        Arrays.sort(data, 0, size);
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
}
