package template;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

public class ObjectList<T> {
    private int size;
    private int cap;
    private Object[] data;
    private static final Object[] EMPTY = new Object[0];

    public ObjectList(int cap) {
        this.cap = cap;
        if (cap == 0) {
            data = EMPTY;
        } else {
            data = new Object[cap];
        }
    }

    public int ceilBinarySearchIndex(T x, Comparator<T> comparator) {
        return SequenceUtils.ceilIndex((T[]) data, x, 0, size - 1, comparator);
    }

    public int floorBinarySearchIndex(T x, Comparator<T> comparator) {
        return SequenceUtils.floorIndex((T[]) data, x, 0, size - 1, comparator);
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

    public ObjectList(ObjectList list) {
        this.size = list.size;
        this.cap = list.cap;
        this.data = Arrays.copyOf(list.data, size);
    }

    public ObjectList() {
        this(0);
    }

    private void ensureSpace(int req) {
        if (req > cap) {
            while (cap < req) {
                cap = Math.max(cap + 10, 2 * cap);
            }
            data = Arrays.copyOf(data, cap);
        }
    }

    public void set(int i, T x) {
        checkRange(i);
        data[i] = x;
    }

    private void checkRange(int i) {
        if (i < 0 || i >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public void expandWith(T x, int len) {
        ensureSpace(len);
        while (size < len) {
            add(x);
        }
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

    public Object[] getData() {
        return data;
    }

    public T get(int i) {
        checkRange(i);
        return (T) data[i];
    }

    public T first() {
        checkRange(0);
        return (T) data[0];
    }

    public T tail() {
        checkRange(0);
        return (T) data[size - 1];
    }

    public void add(T x) {
        ensureSpace(size + 1);
        data[size++] = x;
    }

    public void addAll(T[] x, int offset, int len) {
        ensureSpace(size + len);
        System.arraycopy(x, offset, data, size, len);
        size += len;
    }

    public void addAll(ObjectList list) {
        addAll((T[]) list.data, 0, list.size);
    }

    public int indexOf(T x) {
        for (int i = 0; i < size; i++) {
            if (x == data[i]) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(T x) {
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

    public int binarySearch(T x) {
        return Arrays.binarySearch(data, 0, size, x);
    }

    public void push(T x) {
        add(x);
    }

    public T pop() {
        return (T) data[--size];
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

    public Object[] toArray() {
        return Arrays.copyOf(data, size);
    }

    public T[] toArray(T[] arr) {
        if (arr.length < size) {
            arr = (T[]) Array.newInstance(arr.getClass(), size);
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
