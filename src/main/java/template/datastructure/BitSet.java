package template.datastructure;


import template.utils.SortUtils;

import java.io.Serializable;
import java.util.Arrays;

public final class BitSet implements Serializable, Cloneable, Comparable<BitSet> {
    private long[] data;
    private long tailAvailable;
    private int capacity;
    private int m;

    private static final int SHIFT = 6;
    private static final int LOW = 63;
    private static final int BITS_FOR_EACH = 64;
    private static final long ALL_ONE = ~0L;
    private static final long ALL_ZERO = 0L;
    private static final int MAX_OFFSET = 63;
    private static final int MIN_OFFSET = 0;

    private static final BitSet EMPTY = new BitSet(0);
    private static long[] EMPTY_ARRAY = new long[0];


    public BitSet(int n) {
        capacity = n;
        this.m = (capacity + 64 - 1) / 64;
        data = new long[m];
        tailAvailable = oneBetween(0, offset(capacity - 1));
    }

    public BitSet(BitSet bs) {
        this.data = bs.data.clone();
        this.tailAvailable = bs.tailAvailable;
        this.capacity = bs.capacity;
        this.m = bs.m;
    }

    public BitSet interval(int l, int r) {
        if (r < l) {
            return EMPTY;
        }
        return new BitSet(this, l, r);
    }

    private BitSet(BitSet bs, int l, int r) {
        this.data = EMPTY_ARRAY;
        copyInterval(bs, l, r);
    }

    public void copyInterval(BitSet bs, int l, int r) {
        capacity = r - l + 1;
        tailAvailable = oneBetween(0, offset(capacity - 1));
        int reqLength = word(r) - word(l) + 1;
        if (data.length >= word(r) - word(l) + 1) {
            System.arraycopy(bs.data, word(l), data, 0, reqLength);
        } else {
            data = Arrays.copyOfRange(bs.data, word(l), word(r) + 1);
        }
        this.m = reqLength;
        leftShift(offset(l));
        this.m = (capacity + 64 - 1) / 64;
        data[m - 1] &= tailAvailable;
        for (int i = m; i < reqLength; i++) {
            data[i] = 0;
        }
    }

    public boolean get(int i) {
        return (data[word(i)] & (1L << offset(i))) != 0;
    }

    public void set(int i) {
        data[word(i)] |= (1L << offset(i));
    }

    public void set(int i, boolean val) {
        if (val) {
            set(i);
        } else {
            clear(i);
        }
    }

    private static int word(int i) {
        return i >>> SHIFT;
    }

    private static int offset(int i) {
        return i & LOW;
    }

    private long oneBetween(int l, int r) {
        if (r < l) {
            return 0;
        }
        long lBegin = 1L << offset(l);
        long rEnd = 1L << offset(r);
        return (ALL_ONE ^ (lBegin - 1)) & ((rEnd << 1) - 1);
    }

    public void fill(boolean val) {
        if (val) {
            set(0, capacity() - 1);
        } else {
            clear(0, capacity() - 1);
        }
    }

    public void set(int l, int r) {
        if (r < l) {
            return;
        }
        int lWord = l >>> SHIFT;
        int rWord = r >>> SHIFT;
        for (int i = lWord + 1; i < rWord; i++) {
            data[i] = ALL_ONE;
        }
        //lword
        if (lWord == rWord) {
            data[lWord] |= oneBetween(offset(l), offset(r));
        } else {
            data[lWord] |= oneBetween(offset(l), MAX_OFFSET);
            data[rWord] |= oneBetween(0, offset(r));
        }
    }

    public void clear(int i) {
        data[word(i)] &= ~(1L << offset(i));
    }

    public void inverse(int i) {
        data[word(i)] ^= (1L << offset(i));
    }

    public void clear(int l, int r) {
        if (r < l) {
            return;
        }
        int lWord = l >>> SHIFT;
        int rWord = r >>> SHIFT;
        for (int i = lWord + 1; i < rWord; i++) {
            data[i] = ALL_ZERO;
        }
        //lword
        if (lWord == rWord) {
            data[lWord] &= ~oneBetween(offset(l), offset(r));
        } else {
            data[lWord] &= ~oneBetween(offset(l), MAX_OFFSET);
            data[rWord] &= ~oneBetween(0, offset(r));
        }
    }

    public void flip(int i) {
        data[word(i)] ^= (1L << offset(i));
    }

    public void flip(int l, int r) {
        if (r < l) {
            return;
        }
        int lWord = l >>> SHIFT;
        int rWord = r >>> SHIFT;
        for (int i = lWord + 1; i < rWord; i++) {
            data[i] ^= ALL_ONE;
        }
        //lword
        if (lWord == rWord) {
            data[lWord] ^= oneBetween(offset(l), offset(r));
        } else {
            data[lWord] ^= oneBetween(offset(l), MAX_OFFSET);
            data[rWord] ^= oneBetween(0, offset(r));
        }
    }

    public int capacity() {
        return capacity;
    }

    public boolean isEmpty() {
        return nextSetBit(0) >= capacity();
    }

    public int size() {
        int ans = 0;
        for (long x : data) {
            ans += Long.bitCount(x);
        }
        return ans;
    }

    public int size(int l, int r) {
        if (r < l) {
            return 0;
        }
        int ans = 0;
        int lWord = l >>> SHIFT;
        int rWord = r >>> SHIFT;
        for (int i = lWord + 1; i < rWord; i++) {
            ans += Long.bitCount(data[i]);
        }
        //lword
        if (lWord == rWord) {
            ans += Long.bitCount(data[lWord] & oneBetween(offset(l), offset(r)));
        } else {
            ans += Long.bitCount(data[lWord] & oneBetween(offset(l), MAX_OFFSET));
            ans += Long.bitCount(data[rWord] & oneBetween(0, offset(r)));
        }
        return ans;
    }

    public void copy(BitSet bs) {
        int n = Math.min(this.m, bs.m);
        System.arraycopy(bs.data, 0, data, 0, n);
    }

    public void or(BitSet bs) {
        int n = Math.min(this.m, bs.m);
        for (int i = 0; i < n; i++) {
            data[i] |= bs.data[i];
        }
    }

    public void and(BitSet bs) {
        int n = Math.min(this.m, bs.m);
        for (int i = 0; i < n; i++) {
            data[i] &= bs.data[i];
        }
    }

    public void xor(BitSet bs) {
        int n = Math.min(this.m, bs.m);
        for (int i = 0; i < n; i++) {
            data[i] ^= bs.data[i];
        }
    }

    public int nextSetBit(int start) {
        int offset = offset(start);
        int w = word(start);
        if (offset != 0) {
            long mask = oneBetween(offset, MAX_OFFSET);
            if ((data[w] & mask) != 0) {
                return Long.numberOfTrailingZeros(data[w] & mask) + w * BITS_FOR_EACH;
            }
            w++;
        }

        while (w < m && data[w] == ALL_ZERO) {
            w++;
        }
        if (w >= m) {
            return capacity();
        }
        return Long.numberOfTrailingZeros(data[w]) + w * BITS_FOR_EACH;
    }

    public int prevSetBit(int start) {
        int offset = offset(start);
        int w = word(start);
        if (offset != MAX_OFFSET) {
            long mask = oneBetween(0, offset);
            if ((data[w] & mask) != 0) {
                return MAX_OFFSET - Long.numberOfLeadingZeros(data[w] & mask) + w * BITS_FOR_EACH;
            }
            w--;
        }

        while (w >= 0 && data[w] == ALL_ZERO) {
            w--;
        }
        if (w < 0) {
            return -1;
        }
        return MAX_OFFSET - Long.numberOfLeadingZeros(data[w]) + w * BITS_FOR_EACH;
    }

    public int nextClearBit(int start) {
        int offset = offset(start);
        int w = word(start);
        if (offset != 0) {
            long mask = oneBetween(offset, MAX_OFFSET);
            if ((~data[w] & mask) != 0) {
                return Long.numberOfTrailingZeros(~data[w] & mask) + w * BITS_FOR_EACH;
            }
            w++;
        }

        while (w < m && data[w] == ALL_ONE) {
            w++;
        }
        if (w >= m) {
            return capacity();
        }
        return Long.numberOfTrailingZeros(~data[w]) + w * BITS_FOR_EACH;
    }

    public int prevClearBit(int start) {
        int offset = offset(start);
        int w = word(start);
        if (offset != MAX_OFFSET) {
            long mask = oneBetween(0, offset);
            if ((~data[w] & mask) != 0) {
                return MAX_OFFSET - Long.numberOfLeadingZeros(~data[w] & mask) + w * BITS_FOR_EACH;
            }
            w--;
        }

        while (w >= 0 && data[w] == ALL_ONE) {
            w--;
        }
        if (w < 0) {
            return -1;
        }
        return MAX_OFFSET - Long.numberOfLeadingZeros(~data[w]) + w * BITS_FOR_EACH;
    }

    public void leftShift(int n) {
        int wordMove = word(n);
        int offsetMove = offset(n);
        int rshift = MAX_OFFSET - (offsetMove - 1);

        if (offsetMove != 0) {
            //slightly
            for (int i = 0; i < m; i++) {
                if (i > 0) {
                    data[i - 1] |= data[i] << rshift;
                }
                data[i] >>>= offsetMove;
            }
        }
        if (wordMove > 0) {
            for (int i = 0; i < m; i++) {
                if (i >= wordMove) {
                    data[i - wordMove] = data[i];
                }
                data[i] = 0;
            }
        }
    }

    public void rightShift(int n) {
        int wordMove = word(n);
        int offsetMove = offset(n);
        int lShift = MAX_OFFSET + 1 - offsetMove;

        if (offsetMove != 0) {
            //slightly
            for (int i = m - 1; i >= 0; i--) {
                if (i + 1 < m) {
                    data[i + 1] |= data[i] >>> lShift;
                }
                data[i] <<= offsetMove;
            }
        }
        if (wordMove > 0) {
            for (int i = m - 1; i >= 0; i--) {
                if (i + wordMove < m) {
                    data[i + wordMove] = data[i];
                }
                data[i] = 0;
            }
        }

        data[m - 1] &= tailAvailable;
    }

    @Override
    public BitSet clone() {
        return new BitSet(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        for (int i = nextSetBit(0); i < capacity(); i = nextSetBit(i + 1)) {
            builder.append(i).append(',');
        }
        if (builder.length() > 1) {
            builder.setLength(builder.length() - 1);
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        int ans = 1;
        for (int i = 0; i < m; i++) {
            ans = ans * 31 + Long.hashCode(data[i]);
        }
        return ans;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BitSet)) {
            return false;
        }
        BitSet other = (BitSet) obj;
        if (other.capacity != capacity) {
            return false;
        }
        for (int i = 0; i < m; i++) {
            if (other.data[i] != data[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(BitSet o) {
        if (capacity != o.capacity) {
            return Integer.compare(capacity, o.capacity);
        }
        return SortUtils.compareArray(data, 0, m - 1,
                o.data, 0, m - 1);
    }
}
