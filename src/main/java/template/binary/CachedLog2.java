package template.binary;

/**
 * Log operations
 */
public class CachedLog2 {
    private static final int BITS = 16;
    private static final int LIMIT = 1 << BITS;
    private static final byte[] CACHE = new byte[LIMIT];

    static {
        int b = 0;
        for (int i = 0; i < LIMIT; i++) {
            while ((1 << (b + 1)) <= i) {
                b++;
            }
            CACHE[i] = (byte) b;
        }
    }

    public static int ceilLog(int x) {
        int ans = floorLog(x);
        if ((1 << ans) < x) {
            ans++;
        }
        return ans;
    }

    public static int floorLog(int x) {
        return x < LIMIT ? CACHE[x] : (BITS + CACHE[x >>> BITS]);
    }

    public static int ceilLog(long x) {
        int ans = floorLog(x);
        if ((1L << ans) < x) {
            ans++;
        }
        return ans;
    }

    public static int floorLog(long x) {
        int ans = 0;
        while (x >= LIMIT) {
            ans += BITS;
            x >>>= BITS;
        }
        return ans + CACHE[(int) x];
    }
}
