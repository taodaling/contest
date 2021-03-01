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
        return (r + l) * (r - l + 1) / 2;
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

    public static long pow2(long x){
        return x * x;
    }
}
