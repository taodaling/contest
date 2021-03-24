package template.datastructure;

import template.binary.Bits;
import template.primitve.generated.datastructure.IntegerIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * like treeset, but support O(5) operations and use only O(n/8) memory
 */
public class RangeTree {
    private long[][] data;
    private static int bitShift = 6;
    private static int bitShiftValue = 1 << bitShift;
    private static int bitShiftValueMask = bitShiftValue - 1;
    private int size;
    private int n;

    private int calcLevel(int n) {
        int level = 0;
        while (n > 64) {
            level++;
            n = (n + 63) / 64;
        }
        level++;
        return level;
    }

    public RangeTree(int n) {
        this.n = n;
        int level = calcLevel(n);
        data = new long[level][];
        for (int i = 0, m = (n + 63) / 64; i < level; i++, m = (m + 63) / 64) {
            data[i] = new long[m];
        }
    }

    public boolean contains(int x) {
        return ((data[0][x >> bitShift] >>> (x & bitShiftValueMask)) & 1) == 1;
    }

    public int size() {
        return size;
    }

    public void add(int x) {
        if (contains(x)) {
            return;
        }
        size++;
        for (int i = 0; i < data.length; i++) {
            int offset = x & bitShiftValueMask;
            x >>= bitShift;
            data[i][x] = Bits.set(data[i][x], offset);
        }
    }

    public void remove(int x) {
        if (!contains(x)) {
            return;
        }
        size--;
        long lastValue = 0;
        for (int i = 0; i < data.length && lastValue == 0; i++) {
            int offset = x & bitShiftValueMask;
            x >>= bitShift;
            lastValue = data[i][x] = Bits.clear(data[i][x], offset);
        }
    }



    private int lastSet(int i, int x) {
        assert i < 0 || data[i][x] != 0;
        for (; i >= 0; i--) {
            int offset = Bits.highestOneBitOffset(data[i][x]);
            x = (x << bitShift) | offset;
        }
        return x;
    }

    private int firstSet(int i, int x) {
        assert i < 0 || data[i][x] != 0;
        for (; i >= 0; i--) {
            int offset = Bits.lowestOneBitOffset(data[i][x]);
            x = (x << bitShift) | offset;
        }
        return x;
    }

    private int lastClear(int i, int x) {
        assert i < 0 || data[i][x] != -1;
        for (; i >= 0; i--) {
            int offset = Bits.highestOneBitOffset(~data[i][x]);
            x = (x << bitShift) | offset;
        }
        return x;
    }

    private int firstClear(int i, int x) {
        assert i < 0 || data[i][x] != -1;
        for (; i >= 0; i--) {
            int offset = Bits.lowestOneBitOffset(~data[i][x]);
            x = (x << bitShift) | offset;
        }
        return x;
    }


    public int floor(int x) {
        if (contains(x)) {
            return x;
        }
        for (int i = 0, y = x; i < data.length; i++) {
            int offset = y & bitShiftValueMask;
            y = y >>> bitShift;
            long headMask = Bits.headMask(offset);
            if ((data[i][y] & headMask) != 0) {
                return lastSet(i - 1, (y << bitShift) | Bits.highestOneBitOffset(data[i][y] & headMask));
            }
        }
        return -1;
    }

    public int ceil(int x) {
        if (contains(x)) {
            return x;
        }
        for (int i = 0, y = x; i < data.length; i++) {
            int offset = y & bitShiftValueMask;
            y = y >>> bitShift;
            long tailMask = Bits.tailMask(63 - offset);
            if ((data[i][y] & tailMask) != 0) {
                return firstSet(i - 1, (y << bitShift) | Bits.lowestOneBitOffset(data[i][y] & tailMask));
            }
        }
        return -1;
    }

    public int first() {
        if (size == 0) {
            return -1;
        }
        return firstSet(data.length - 1, 0);
    }

    public int last() {
        if (size == 0) {
            return -1;
        }
        return lastSet(data.length - 1, 0);
    }

    public int mex() {
        if (size == n) {
            return n;
        }
        return firstClear(data.length - 1, 0);
    }

    public IntegerIterator iterator() {
        return iterator(0, n - 1);
    }

    public IntegerIterator iterator(int l, int r) {
        return new IntegerIterator() {
            int cur = ceil(l);

            @Override
            public boolean hasNext() {
                return cur <= r && cur != -1;
            }

            @Override
            public int next() {
                int ans = cur;
                if (cur == r) {
                    cur = -1;
                } else {
                    cur = ceil(cur + 1);
                }
                assert cur == -1 || cur > ans;
                return ans;
            }
        };
    }


    @Override
    public String toString() {
        IntegerIterator iterator = iterator();
        List<Integer> list = new ArrayList<>(n);
        for (; iterator.hasNext(); ) {
            list.add(iterator.next());
        }
        return list.toString();
    }

    public void clear() {
        size = 0;
        for (int i = 0; i < data.length; i++) {
            Arrays.fill(data[i], 0);
        }
    }
}
