package template.math;

public class IntMath {
    private IntMath() {
    }

    /**
     * l + (l + 1) + ... + r
     */
    public static long sumOfInterval(long l, long r) {
        if (l > r) {
            return 0;
        }
        long a = r + l;
        long b = r - l + 1;
        if ((a & 1) == 0) {
            return (a >> 1) * b;
        } else {
            return a * (b >> 1);
        }
    }

    /**
     * l^2 + (l + 1)^2 + ... + r^2
     */
    public static long sumOfInterval2(long l, long r) {
        if (r < l) {
            return 0;
        }
        return (r * (r + 1) * (2 * r + 1) - (l - 1) * l * (2 * l - 1)) / 6;
    }

    /**
     * 1^2 + 2^2 + ... + n^2
     */
    public static long sumOfInterval2(long n) {
        if (n <= 0) {
            return 0;
        }
        return n * (n + 1) * (2 * n + 1) / 6;
    }

    /**
     * <pre>
     * https://atcoder.jp/contests/practice2/submissions/16564332
     * find b that ab%mod=1
     * </pre>
     */
    public static long invl(long a, long mod) {
        long b = mod;
        long p = 1, q = 0;
        while (b > 0) {
            long c = a / b;
            long d;
            d = a;
            a = b;
            b = d % b;
            d = p;
            p = q;
            q = d - c * q;
        }
        return p < 0 ? p + mod : p;
    }

    public static long pow2(long x) {
        return x * x;
    }

    public static int ceilSqrt(long x) {
        int ans = floorSqrt(x);
        if ((long) ans * ans < x) {
            ans++;
        }
        return ans;
    }

    public static int floorSqrt(long x) {
        int lo = 0;
        int hi = (int) 2e9;

        while (lo < hi) {
            int mid = (lo + hi + 1) >> 1;
            if ((long) mid * mid <= x) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }

        return lo;
    }
}
