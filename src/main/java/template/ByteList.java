package template;

import java.util.Arrays;

public class ByteList {
    private int size;
    private int cap;
    private byte[] data;
    private static final byte[] EMPTY = new byte[0];

    public ByteList(int cap) {
        this.cap = cap;
        if (cap == 0) {
            data = EMPTY;
        } else {
            data = new byte[cap];
        }
    }

    public ByteList(ByteList list) {
        this.size = list.size;
        this.cap = list.cap;
        this.data = Arrays.copyOf(list.data, size);
    }

    public ByteList() {
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

    public byte get(int i) {
        checkRange(i);
        return data[i];
    }

    public void add(byte x) {
        ensureSpace(1);
        data[size++] = x;
    }

    public void addAll(byte[] x, int offset, int len) {
        ensureSpace(len);
        System.arraycopy(x, offset, data, size, len);
        size += len;
    }

    public void addAll(ByteList list) {
        addAll(list.data, 0, list.size);
    }

    public int indexOf(byte x) {
        for (int i = 0; i < size; i++) {
            if (x == data[i]) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(byte x) {
        for (int i = size - 1; i >= 0; i--) {
            if (x == data[i]) {
                return i;
            }
        }
        return -1;
    }

    public void sort() {
        Randomized.randomizedArray(data, 0, size);
        Arrays.sort(data, 0, size);
    }

    public int binarySearch(byte x) {
        return Arrays.binarySearch(data, 0, size, x);
    }

    public void push(byte x) {
        add(x);
    }

    public void set(int i, byte x){
        checkRange(i);
        data[i] = x;
    }

    public byte pop() {
        return data[--size];
    }

    public int size() {
        return size;
    }

    public void expandWith(byte x, int len) {
        ensureSpace(len - size);
        while (size < len) {
            add(x);
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