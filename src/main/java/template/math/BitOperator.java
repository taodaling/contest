package template.math;

/**
 * Bit operations
 */
public class BitOperator {
    public int bitAt(int x, int i) {
        return (x >> i) & 1;
    }

    public int bitAt(long x, int i) {
        return (int) ((x >> i) & 1);
    }

    public int setBit(int x, int i, boolean v) {
        if (v) {
            x |= 1 << i;
        } else {
            x &= ~(1 << i);
        }
        return x;
    }

    public long setBit(long x, int i, boolean v) {
        if (v) {
            x |= 1L << i;
        } else {
            x &= ~(1L << i);
        }
        return x;
    }

    public long swapBit(long x, int i, int j) {
        int bi = bitAt(x, i);
        int bj = bitAt(x, j);
        x = setBit(x, i, bj == 1);
        x = setBit(x, j, bi == 1);
        return x;
    }

    public int swapBit(int x, int i, int j) {
        int bi = bitAt(x, i);
        int bj = bitAt(x, j);
        x = setBit(x, i, bj == 1);
        x = setBit(x, j, bi == 1);
        return x;
    }

    /**
     * Determine whether x is subset of y
     */
    public boolean subset(long x, long y) {
        return intersect(x, y) == x;
    }

    /**
     * Merge two set
     */
    public long merge(long x, long y) {
        return x | y;
    }

    public long intersect(long x, long y) {
        return x & y;
    }

    public int intersect(int x, int y) {
        return x & y;
    }

    public long differ(long x, long y) {
        return x - intersect(x, y);
    }

    public int differ(int x, int y) {
        return x - intersect(x, y);
    }
}
