package template;

import java.util.Map;
import java.util.TreeMap;

public class Bitset {
    private long[] bits;
    private int offset; // 0 => offset
    private static final int MASK = 64 - 1;

    public Bitset(int n) {
        this.bits = new long[(n + 64 - 1) / 64];
    }

    public Bitset(Bitset origin) {
        bits = origin.bits.clone();
        offset = origin.offset;
    }

    private Bitset(Bitset origin, int offset) {
        this.bits = origin.bits;
        this.offset = origin.offset + offset;
    }

    private int bitCount() {
        return bits.length << 6;
    }

    private boolean bitAt(long v, int i) {
        return ((v >>> i) & 1) == 1;
    }

    private long setBit(long v, int i, boolean flag) {
        if (flag) {
            return v | (1L << i);
        }
        return v & ~(1L << i);
    }

    private int getFixedIndex(int i) {
        return i + offset;
    }

    private boolean outOfBound(int i) {
        return i < 0 || i >= bitCount();
    }

    private int whichBlock(int i) {
        return i >> 8;
    }

    private long getBlock(int i) {
        if (i < 0 || i >= bits.length) {
            return 0L;
        }
        return bits[i];
    }

    public boolean get(int i) {
        i = getFixedIndex(i);
        if (outOfBound(i)) {
            return false;
        }
        return bitAt(bits[whichBlock(i)], i & MASK);
    }

    public void set(int i, boolean v) {
        i = getFixedIndex(i);
        if (outOfBound(i)) {
            throw new IndexOutOfBoundsException();
        }
        int j = whichBlock(i);
        bits[j] = setBit(bits[j], i & MASK, v);
    }

    public Bitset rightShiftView(int offset) {
        return new Bitset(this, -offset);
    }

    public Bitset leftShiftView(int offset) {
        return new Bitset(this, offset);
    }

    /**
     * Get a subset of range [l, l + 64)
     */
    private long blockAt(int l) {
        l = getFixedIndex(l);
        int blockId = whichBlock(l);
        int shift = l & MASK;
        return (getBlock(blockId) >>> shift)
                | (getBlock(blockId + 1) << (64 - shift));
    }

    private void setBlock(int l, long b) {
        l = getFixedIndex(l);
        int blockId = whichBlock(l);
        int shift = l & MASK;
        if (shift == 0) {
            bits[blockId] = b;
        } else {
            bits[blockId] &= ((1 << shift) - 1) | b >> shift;
            bits[blockId + 1] &= -(1 << shift) | (b >>> (64 - shift));
        }
    }

    public static boolean intersect(Bitset a, Bitset b) {
        int n = a.bitCount();
        int m = b.bitCount();
        for (int i = 0, until = Math.min(n, m); i < until; i += 64) {
            if ((a.blockAt(i) & b.blockAt(i)) != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        Map<Integer, Boolean> map = new TreeMap<>();
        for (int i = 0; i < bitCount(); i++) {
            map.put(i, get(i));
        }
        return map.toString();
    }
}
