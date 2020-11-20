package template.primitve.generated.datastructure;

import template.datastructure.FixedMinHeap;
import template.utils.CompareUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class DoubleFixedMinHeap {
    private double[] data;
    private static double[] buf = new double[1 << 8];
    private DoubleComparator comparator;
    private int size;

    public DoubleFixedMinHeap(int cap, DoubleComparator comparator) {
        if (cap <= 0 || cap * 2 > buf.length) {
            throw new IllegalArgumentException();
        }
        data = new double[cap + 1];
        this.comparator = comparator;
    }

    public void add(double x) {
        int i;
        for (i = size - 1; i >= 0 && comparator.compare(data[i], x) > 0; i--) {
            data[i + 1] = data[i];
        }
        data[i + 1] = x;
        size = Math.min(size + 1, data.length - 1);
    }

    private void checkRange(int k) {
        if (k < 0 || k >= size) {
            throw new ArrayIndexOutOfBoundsException("Access " + k + "-th element");
        }
    }

    public void removeKthSmallest(int k) {
        checkRange(k);
        for (int i = k + 1; i < size; i++) {
            data[i - 1] = data[i];
        }
        size--;
    }

    public void addAll(DoubleFixedMinHeap other) {
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
    public double getKthSmallest(int k) {
        checkRange(k);
        return data[k];
    }

    public void fill(double x, int n) {
        n = Math.min(n, data.length - 1);
        Arrays.fill(data, 0, n, x);
        size = n;
    }

    public DoubleIterator iterator() {
        return new DoubleIterator() {
            int cur = 0;

            @Override
            public boolean hasNext() {
                return cur < size;
            }

            @Override
            public double next() {
                return data[cur++];
            }
        };
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
