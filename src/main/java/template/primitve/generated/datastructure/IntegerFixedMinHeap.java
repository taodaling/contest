package template.primitve.generated.datastructure;

import template.datastructure.FixedMinHeap;
import template.utils.CompareUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class IntegerFixedMinHeap {
    private int[] data;
    private static int[] buf = new int[1 << 8];
    private IntegerComparator comparator;
    private int size;

    public IntegerFixedMinHeap(int cap, IntegerComparator comparator) {
        if (cap <= 0 || cap * 2 > buf.length) {
            throw new IllegalArgumentException();
        }
        data = new int[cap + 1];
        this.comparator = comparator;
    }

    public void add(int x) {
        int i;
        for (i = size - 1; i >= 0 && comparator.compare(data[i], x) > 0; i--) {
            data[i + 1] = data[i];
        }
        data[i + 1] = x;
        size = Math.min(size + 1, data.length - 1);
    }

    public void addAll(IntegerFixedMinHeap other) {
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
    public int getKthSmallest(int k) {
        return data[k];
    }

    public void fill(int x, int n) {
        n = Math.min(n, data.length - 1);
        Arrays.fill(data, 0, n, x);
        size = n;
    }

    public IntegerIterator iterator() {
        return new IntegerIterator() {
            int cur = 0;

            @Override
            public boolean hasNext() {
                return cur < size;
            }

            @Override
            public int next() {
                return data[cur++];
            }
        };
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
