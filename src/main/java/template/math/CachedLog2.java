package template.math;

import template.datastructure.BIT;

/**
 * Log operations
 */
public class CachedLog2 {
    private static final int BITS = 16;
    private static final int LIMIT = 1 << BITS;
    private static final int[] CACHE = new int[LIMIT];

    static {
        int b = 0;
        for (int i = 0; i < LIMIT; i++) {
            while ((1 << (b + 1)) <= i) {
                b++;
            }
            CACHE[i] = b;
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
}
