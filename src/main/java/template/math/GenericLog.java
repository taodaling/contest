package template.math;

public class GenericLog {
    /**
     * Find log_x y
     */
    public int floorLog(long x, long y) {
        if (x <= 1) {
            throw new IllegalArgumentException();
        }
        int ans = 0;
        while (y >= x) {
            ans++;
            y /= x;
        }
        return ans;
    }

    /**
     * Find log_x y
     */
    public int ceilLog(long x, long y) {
        if (x <= 1) {
            throw new IllegalArgumentException();
        }
        int ans = 0;
        while (y > 1) {
            ans++;
            y /= x;
        }
        return ans;
    }
}
