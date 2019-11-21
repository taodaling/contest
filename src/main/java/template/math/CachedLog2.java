package template.math;

/**
 * Log operations
 */
public class CachedLog2 {
    public CachedLog2(int n) {
        cache = new int[n + 1];
        int b = 0;
        for (int i = 0; i <= n; i++) {
            while ((1 << (b + 1)) <= i) {
                b++;
            }
            cache[i] = b;
        }
    }

    private int[] cache;
    private Log2 log2;

    public int ceilLog(int x) {
        int ans = floorLog(x);
        if ((1 << ans) < x) {
            x++;
        }
        return x;
    }

    public int floorLog(int x) {
        if (x >= cache.length) {
            return log2.floorLog(x);
        }
        return cache[x];
    }
}
