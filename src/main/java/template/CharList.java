package template;

import java.util.Arrays;

public class CharList {
    private int size;
    private int cap;
    private char[] data;
    private static final char[] EMPTY = new char[0];

    public CharList(int cap) {
        this.cap = cap;
        if (cap == 0) {
            data = EMPTY;
        } else {
            data = new char[cap];
        }
    }

    public CharList(CharList list) {
        this.size = list.size;
        this.cap = list.cap;
        this.data = Arrays.copyOf(list.data, size);
    }

    public CharList() {
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

    public char get(int i) {
        checkRange(i);
        return data[i];
    }

    public void add(char x) {
        ensureSpace(1);
        data[size++] = x;
    }

    public void addAll(char[] x, int offset, int len) {
        ensureSpace(len);
        System.arraycopy(x, offset, data, size, len);
        size += len;
    }

    public void expandWith(char x, int len) {
        ensureSpace(len - size);
        while (size < len) {
            add(x);
        }
    }

    public void set(int i, char x){
        checkRange(i);
        data[i] = x;
    }

    public void addAll(CharList list) {
        addAll(list.data, 0, list.size);
    }

    public int indexOf(char x) {
        for (int i = 0; i < size; i++) {
            if (x == data[i]) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(char x) {
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

    public int binarySearch(char x) {
        return Arrays.binarySearch(data, 0, size, x);
    }

    public void push(char x) {
        add(x);
    }

    public char pop() {
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


    public char[] toArray() {
        return Arrays.copyOf(data, size);
    }

    public char[] toArray(char[] arr) {
        if (arr.length < size) {
            arr = new char[size];
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
