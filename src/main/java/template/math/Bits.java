package template.math;

/**
 * Bit operations
 */
public class Bits {
    private Bits() {
    }

    public static int bitAt(int x, int i) {
        return (x >> i) & 1;
    }

    public static int bitAt(long x, int i) {
        return (int) ((x >> i) & 1);
    }

    public static int setBit(int x, int i, boolean v) {
        if (v) {
            x |= 1 << i;
        } else {
            x &= ~(1 << i);
        }
        return x;
    }

    public static long setBit(long x, int i, boolean v) {
        if (v) {
            x |= 1L << i;
        } else {
            x &= ~(1L << i);
        }
        return x;
    }

    public static long swapBit(long x, int i, int j) {
        int bi = bitAt(x, i);
        int bj = bitAt(x, j);
        x = setBit(x, i, bj == 1);
        x = setBit(x, j, bi == 1);
        return x;
    }

    public static int swapBit(int x, int i, int j) {
        int bi = bitAt(x, i);
        int bj = bitAt(x, j);
        x = setBit(x, i, bj == 1);
        x = setBit(x, j, bi == 1);
        return x;
    }

    /**
     * Determine whether x is subset of y
     */
    public static boolean subset(long x, long y) {
        return intersect(x, y) == x;
    }

    /**
     * Merge two set
     */
    public static long merge(long x, long y) {
        return x | y;
    }

    public static long intersect(long x, long y) {
        return x & y;
    }

    public static int intersect(int x, int y) {
        return x & y;
    }

    public static long differ(long x, long y) {
        return x - intersect(x, y);
    }

    public static int differ(int x, int y) {
        return x - intersect(x, y);
    }

    public static int longestCommonPrefix(int x, int y) {
        return x >>> CachedLog2.floorLog(x ^ y);
    }

    public static int theFirstDifferentIndex(int x, int y) {
        return CachedLog2.floorLog(x ^ y);
    }

    public static int lowestBit(int x) {
        return x & -x;
    }

    public static int highestBit(int x) {
        return 1 << CachedLog2.floorLog(x);
    }
}
