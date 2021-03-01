package template.datastructure;

import template.utils.CompareUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class FixedMinHeap<T> implements Iterable<T> {
    private Object[] data;
    private static Object[] buf = new Object[1 << 8];
    private Comparator<T> comparator;
    private int size;

    public T[] toArray(T[] data) {
        if (data.length < size) {
            data = (T[]) Array.newInstance(data.getClass().getComponentType(), size);
        }
        System.arraycopy(buf, 0, data, 0, size);
        return data;
    }

    public FixedMinHeap(int cap, Comparator<T> comparator) {
        if (cap <= 0 || cap * 2 > buf.length) {
            throw new IllegalArgumentException();
        }
        data = new Object[cap + 1];
        this.comparator = comparator;
    }

    public void add(T x) {
        int i;
        for (i = size - 1; i >= 0 && comparator.compare((T) data[i], x) > 0; i--) {
            data[i + 1] = data[i];
        }
        data[i + 1] = x;
        size = Math.min(size + 1, data.length - 1);
    }

    public void addAll(FixedMinHeap<T> other) {
        int newLen = Math.min(size + other.size, data.length - 1);
        CompareUtils.mergeAscending(data, 0, size - 1, other.data, 0, other.size - 1,
                buf, 0, comparator);
        System.arraycopy(buf, 0, data, 0, newLen);
    }

    public int size() {
        return size;
    }

    public void clear() {
        size = 0;
    }

    /**
     * start with 0
     */
    public T get(int k) {
        return (T) data[k];
    }

    public void fill(T x, int n) {
        n = Math.min(n, data.length - 1);
        Arrays.fill(data, 0, n, x);
        size = n;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int cur = 0;

            @Override
            public boolean hasNext() {
                return cur < size;
            }

            @Override
            public T next() {
                return (T) data[cur++];
            }
        };
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
