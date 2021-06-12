package template.binary;

/**
 * Bit operations
 */
public class Bits {
    private Bits() {
    }

    public static int get(int x, int i) {
        return (x >>> i) & 1;
    }

    public static int get(long x, int i) {
        return (int) ((x >>> i) & 1);
    }

    public static int set(int x, int i) {
        return x | (1 << i);
    }

    public static int clear(int x, int i) {
        return x & (~(1 << i));
    }

    public static long set(long x, int i) {
        return x | (1L << i);
    }

    public static long clear(long x, int i) {
        return x & (~(1L << i));
    }

    public static int set(int x, int i, boolean v) {
        if (v) {
            return set(x, i);
        } else {
            return clear(x, i);
        }
    }

    public static long set(long x, int i, boolean v) {
        if (v) {
            return set(x, i);
        } else {
            return clear(x, i);
        }
    }

    public static long flip(long x, int i) {
        return x ^ (1L << i);
    }

    public static int flip(int x, int i) {
        return x ^ (1 << i);
    }

    public static long swap(long x, int i, int j) {
        int bi = get(x, i);
        int bj = get(x, j);
        x = set(x, i, bj == 1);
        x = set(x, j, bi == 1);
        return x;
    }

    public static int swap(int x, int i, int j) {
        int bi = get(x, i);
        int bj = get(x, j);
        x = set(x, i, bj == 1);
        x = set(x, j, bi == 1);
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

    public static int merge(int x, int y) {
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
        return x >>> Log2.floorLog(x ^ y);
    }

    public static int theFirstDifferentIndex(int x, int y) {
        return Log2.floorLog(x ^ y);
    }

    public static int theFirstDifferentIndex(long x, long y) {
        return Log2.floorLog(x ^ y);
    }

    public static int lowestBit(int x) {
        return x & -x;
    }

    public static int highestBit(int x) {
        return 1 << Log2.floorLog(x);
    }

    public static int highestOneBitOffset(long x) {
        if (x < 0) {
            return 63;
        }
        return 63 - Long.numberOfLeadingZeros(x);
    }

    public static int lowestOneBitOffset(long x) {
        return highestOneBitOffset(x & -x);
    }

    public static long tailMask(int n) {
        if (n == 0) {
            return 0;
        }
        return -1L << (64 - n);
    }

    public static long headMask(int n) {
        if (n == 0) {
            return 0;
        }
        return -1L >>> (64 - n);
    }

    public static long leftShift(long x, int bit){
        return x << (bit & 63);
    }


    public static long rightShift(long x, int bit){
        return x >>> (bit & 63);
    }
}
