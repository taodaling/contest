package template.primitve.generated.datastructure;

import template.algo.BinarySearch;
import template.rand.Randomized;
import template.utils.SortUtils;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class DoubleArrayList implements Cloneable {
    private int size;
    private int cap;
    private double[] data;
    private static final double[] EMPTY = new double[0];

    public double[] getData() {
        return data;
    }

    public void remove(int l, int r) {
        if (l > r) {
            return;
        }
        checkRange(l);
        checkRange(r);
        if (r == size - 1) {
            size = l;
            return;
        } else {
            System.arraycopy(data, r + 1, data, l, size - (r + 1));
            size -= (r - l + 1);
        }
    }

    public DoubleArrayList(int cap) {
        this.cap = cap;
        if (cap == 0) {
            data = EMPTY;
        } else {
            data = new double[cap];
        }
    }

    public DoubleArrayList(double[] data) {
        this(0);
        addAll(data);
    }

    public void addAll(DoubleIterator iterator) {
        while (iterator.hasNext()) {
            add(iterator.next());
        }
    }

    public DoubleArrayList(DoubleArrayList list) {
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

    public DoubleArrayList() {
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
            throw new ArrayIndexOutOfBoundsException("Access [" + i + "]");
        }
    }

    public double get(int i) {
        checkRange(i);
        return data[i];
    }

    public void add(double x) {
        ensureSpace(size + 1);
        data[size++] = x;
    }

    public void addAll(double[] x) {
        addAll(x, 0, x.length);
    }

    public void addAll(double[] x, int offset, int len) {
        ensureSpace(size + len);
        System.arraycopy(x, offset, data, size, len);
        size += len;
    }

    public void addAll(DoubleArrayList list) {
        addAll(list.data, 0, list.size);
    }

    public int indexOf(double x) {
        for (int i = 0; i < size; i++) {
            if (x == data[i]) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(double x) {
        for (int i = size - 1; i >= 0; i--) {
            if (x == data[i]) {
                return i;
            }
        }
        return -1;
    }

    public void expandWith(double x, int len) {
        ensureSpace(len);
        while (size < len) {
            data[size++] = x;
        }
    }

    public void sort() {
        if (size <= 1) {
            return;
        }
        Randomized.shuffle(data, 0, size);
        Arrays.sort(data, 0, size);
    }

    public void sort(DoubleComparator comparator) {
        SortUtils.quickSort(data, comparator, 0, size);
    }

    public double first() {
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

    public double tail() {
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

    public void set(int i, double x) {
        checkRange(i);
        data[i] = x;
    }

    public int binarySearch(double x) {
        return Arrays.binarySearch(data, 0, size, x);
    }

    public int lowerBound(double x) {
        return BinarySearch.lowerBound(data, 0, size - 1, x);
    }

    public int upperBound(double x) {
        return BinarySearch.upperBound(data, 0, size - 1, x);
    }

    public void push(int x) {
        add(x);
    }

    public double pop() {
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
        System.arraycopy(data, index + 1, data, index, size - (index + 1));
        size--;
    }

    public double[] toArray() {
        return Arrays.copyOf(data, size);
    }

    public double[] toArray(double[] arr) {
        if (arr.length < size) {
            arr = new double[size];
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

    public DoubleIterator iterator() {
        return new DoubleIterator() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public double next() {
                return data[i++];
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DoubleArrayList)) {
            return false;
        }
        DoubleArrayList other = (DoubleArrayList) obj;
        return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
    }

    @Override
    public int hashCode() {
        int h = 1;
        for (int i = 0; i < size; i++) {
            h = h * 31 + Double.hashCode(data[i]);
        }
        return h;
    }

    @Override
    public DoubleArrayList clone() {
        DoubleArrayList ans = new DoubleArrayList();
        ans.addAll(this);
        return ans;
    }
}
